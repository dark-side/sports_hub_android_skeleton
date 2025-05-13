@file:Suppress("unused")

package com.softserveinc.sportshub.domain.model.common

import com.softserveinc.sportshub.domain.model.common.ResultState.Companion.toSuccessResultState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed class ResultState<out T> {

    data class Success<out T>(val value: T) : ResultState<T>()

    data class Failure(val appErrors: ImmutableList<AppError>) : ResultState<Nothing>()

    // TODO: Add optional progress?
    // TODO: Convert to data class?
    data object Loading : ResultState<Nothing>()

    companion object {

        fun failure(vararg appErrors: AppError): Failure {
            return Failure(appErrors.toImmutableList())
        }

        fun <T> T.toSuccessResultState(): ResultState<T> {
            return Success(this)
        }

        fun <T> nullResultState(): ResultState<T?> {
            return Success(null)
        }
    }
}

inline fun <T> nullResultStateFlow(): Flow<ResultState<T?>> {
    return flowOf(ResultState.nullResultState())
}

fun <T> successFlowOf(value: T): Flow<ResultState<T>> {
    return flowOf(value.toSuccessResultState())
}

inline val <T> ResultState<T>.isSuccess: Boolean?
    get() {
        return when (this) {
            is ResultState.Failure -> false
            is ResultState.Success -> true
            ResultState.Loading -> null
        }
    }

inline val <T> ResultState<T>.isLoading: Boolean
    get() {
        return when (this) {
            is ResultState.Failure -> false
            is ResultState.Success -> false
            ResultState.Loading -> true
        }
    }

fun <T> ResultState<T>.getOrThrow(): T {
    return when (this) {
        is ResultState.Success -> value
        // TODO: Refine .toString() logic
        is ResultState.Failure -> throw Exception(appErrors.joinToString { it.toString() })
        ResultState.Loading -> throw Exception("Loading in progress")
    }
}

fun <T> ResultState<T>.getOrNull(): T? {
    return when (this) {
        is ResultState.Success -> value
        is ResultState.Failure -> null
        ResultState.Loading -> null
    }
}

fun <R, T : R> ResultState<T>.getOrElse(
    onFailure: (failure: ResultState.Failure) -> R,
    onLoading: () -> R,
): R {
    return when (this) {
        is ResultState.Success -> value
        is ResultState.Failure -> onFailure(this)
        ResultState.Loading -> onLoading()
    }
}

fun ResultState<*>.failureOrNull(): ResultState.Failure? {
    return when (this) {
        is ResultState.Success -> null
        is ResultState.Failure -> this
        ResultState.Loading -> null
    }
}

fun ResultState<*>.failure(): ResultState.Failure {
    return failureOrNull() ?: throw IllegalStateException()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T, reified V> ResultState<T>.mapSuccess(
    crossinline transformer: (T) -> V,
): ResultState<V> {
    if (this is ResultState.Success<T>) {
        return ResultState.Success(transformer(value))
    }
    return this as ResultState<V>
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T, reified V> ResultState<T>.mapSuccessSuspendValue(
    crossinline transformer: suspend (T) -> V
): ResultState<V> {
    return when (this) {
        is ResultState.Success<T> -> {
            // Here we're already in a suspend context, so `transformer` can be suspend.
            val newValue = transformer(value)
            ResultState.Success(newValue)
        }

        else -> this as ResultState<V>
    }
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T, reified V> ResultState<T>.mapSuccessSuspend(
    crossinline transformer: suspend (T) -> ResultState<V>
): ResultState<V> {
    return when (this) {
        is ResultState.Success<T> -> {
            transformer(value)
        }

        else -> this as ResultState<V>
    }
}

inline fun <reified T, reified V> ResultState<T>.flatMapSuccess(
    crossinline transformer: (T) -> ResultState<V>,
): ResultState<V> {
    return when (this) {
        is ResultState.Success<T> -> transformer(value)
        is ResultState.Failure -> this
        ResultState.Loading -> ResultState.Loading
    }
}

suspend inline fun <reified T, reified V> ResultState<T>.mapSuspend(
    crossinline transformer: suspend (ResultState<T>) -> ResultState<V>,
): ResultState<V> {
    return transformer(this)
}

suspend inline fun <reified T, reified V> ResultState<T>.flatMapSuccessSuspend(
    crossinline transformer: suspend (T) -> ResultState<V>,
): ResultState<V> {
    return when (this) {
        is ResultState.Success<T> -> transformer(value)
        is ResultState.Failure -> this
        ResultState.Loading -> ResultState.Loading
    }
}

inline fun <reified T, reified V> ResultState<T>.flatMapFlowSuccess(
    crossinline transform: (T) -> Flow<ResultState<V>>
): Flow<ResultState<V>> {
    return when (this) {
        is ResultState.Success -> transform(value)
        is ResultState.Failure -> flowOf(this)
        ResultState.Loading -> flowOf(ResultState.Loading)
    }
}

suspend inline fun <reified T, reified V> ResultState<T>.flatMapFlowSuccessSuspend(
    crossinline transform: suspend (T) -> Flow<ResultState<V>>
): Flow<ResultState<V>> {
    return when (this) {
        is ResultState.Success -> transform(value)
        is ResultState.Failure -> flowOf(this)
        ResultState.Loading -> flowOf(ResultState.Loading)
    }
}

/**
 * Transforms a list of [ResultState] objects into a single [ResultState] containing a list of all successful values.
 *
 * - If all elements are [ResultState.Success], returns [ResultState.Success] with a list of all values.
 * - If any element is [ResultState.Failure], returns the first encountered [ResultState.Failure].
 * - If any element is [ResultState.Loading] and no [ResultState.Failure] is present, returns [ResultState.Loading].
 *
 * @receiver The list of [ResultState] to combine.
 * @return A single [ResultState] representing the combined state.
 */
fun <T> List<ResultState<T>>.combineResultStates(): ResultState<List<T>> {
    val combinedList = mutableListOf<T>()

    for (result in this) {
        when (result) {
            is ResultState.Success -> combinedList.add(result.value)
            is ResultState.Failure -> return result
            is ResultState.Loading -> return ResultState.Loading
        }
    }

    return ResultState.Success(combinedList)
}

fun <K, T> Map<K, ResultState<T>>.combineResultStates(): ResultState<Map<K, T>> {
    val combined = mutableMapOf<K, T>()

    for ((key, state) in this) {
        when (state) {
            is ResultState.Success -> combined[key] = state.value
            is ResultState.Failure -> return state
            is ResultState.Loading -> return ResultState.Loading
        }
    }

    return ResultState.Success(combined)
}

fun <A, B> Pair<ResultState<A>, ResultState<B>>.combineResultStates(): ResultState<Pair<A, B>> {
    val (firstState, secondState) = this

    return when (firstState) {
        is ResultState.Failure -> firstState
        is ResultState.Loading -> ResultState.Loading
        is ResultState.Success -> {
            when (secondState) {
                is ResultState.Failure -> secondState
                is ResultState.Loading -> ResultState.Loading
                is ResultState.Success ->
                    ResultState.Success(firstState.value to secondState.value)
            }
        }
    }
}

/**
 * Flattens a nested [ResultState] (i.e., [ResultState<ResultState<T>>]) into a single [ResultState<T>].
 *
 * - If the outer [ResultState] is [ResultState.Success], it returns the inner [ResultState].
 * - If the outer [ResultState] is [ResultState.Failure] or [ResultState.Loading], it returns the outer state directly.
 *
 * @receiver The nested [ResultState] to flatten.
 * @return A single [ResultState] representing the flattened state.
 */
fun <T> ResultState<ResultState<T>>.flatten(): ResultState<T> = when (this) {
    is ResultState.Success -> this.value // Inner ResultState<T>
    is ResultState.Failure -> this // Outer Failure
    is ResultState.Loading -> this // Outer Loading
}

fun <T> ResultState<List<ResultState<T>>>.flattenListOfResultStates(): List<ResultState<T>> {
    return when (this) {
        is ResultState.Success -> this.value
        is ResultState.Failure -> listOf(this)
        ResultState.Loading -> listOf(ResultState.Loading)
    }
}
