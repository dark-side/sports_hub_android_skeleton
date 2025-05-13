@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("unused")

package com.softserveinc.sportshub.util.flow

import com.softserveinc.sportshub.domain.model.common.AppError
import com.softserveinc.sportshub.domain.model.common.ResultState
import com.softserveinc.sportshub.domain.model.common.ResultState.Companion.toSuccessResultState
import com.softserveinc.sportshub.domain.model.common.combineResultStates
import com.softserveinc.sportshub.domain.model.common.failureOrNull
import com.softserveinc.sportshub.domain.model.common.isSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformWhile

suspend fun <R> Flow<ResultState<R>>.firstResultValue(): ResultState<R> {
    return first { it.isSuccess != null }
}

fun <R> Flow<R>.takeUntil(transform: suspend FlowCollector<R>.(value: R) -> Boolean): Flow<R> {
    return transformWhile {
        emit(it)
        !transform(it)
    }
}

fun <T, R> Flow<ResultState<T>>.mapSuccess(
    transform: (T) -> R,
): Flow<ResultState<R>> = map { resultState ->
    when (resultState) {
        is ResultState.Success -> transform(resultState.value).toSuccessResultState()
        is ResultState.Failure -> resultState
        ResultState.Loading -> ResultState.Loading
    }
}

fun <T, R> Flow<ResultState<T>>.suspendMapSuccessValue(
    transform: suspend (T) -> R,
): Flow<ResultState<R>> = map { resultState ->
    when (resultState) {
        is ResultState.Success -> transform(resultState.value).toSuccessResultState()
        is ResultState.Failure -> resultState
        ResultState.Loading -> ResultState.Loading
    }
}

fun <T, R> Flow<ResultState<T>>.suspendMapSuccess(
    transform: suspend (T) -> ResultState<R>,
): Flow<ResultState<R>> = map { resultState ->
    when (resultState) {
        is ResultState.Success -> transform(resultState.value)
        is ResultState.Failure -> resultState
        ResultState.Loading -> ResultState.Loading
    }
}

fun <A, B> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
): Flow<ResultState<CombinedResult2<A, B>>> = combineResultStateFlows(
    flowA, flowB
) { a, b ->
    CombinedResult2(a, b)
}

fun <A, B, K> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    transform: suspend (A, B) -> K,
): Flow<ResultState<K>> = flowA.combine(flowB) { resultStateA, resultStateB ->
    if (resultStateA is ResultState.Loading || resultStateB is ResultState.Loading) {
        ResultState.Loading
    } else if (resultStateA is ResultState.Success && resultStateB is ResultState.Success) {
        ResultState.Success(transform(resultStateA.value, resultStateB.value))
    } else {
        ResultState.Failure(
            buildList {
                addAll(resultStateA.failureOrNull()?.appErrors.orEmpty())
                addAll(resultStateB.failureOrNull()?.appErrors.orEmpty())
            }.toImmutableList()
        )
    }
}

fun <T, R> Flow<ResultState<T>>.flatMapLatestSuccess(
    transform: (T) -> Flow<ResultState<R>>
): Flow<ResultState<R>> = flatMapLatest { resultState ->
    when (resultState) {
        is ResultState.Success -> transform(resultState.value)
        is ResultState.Failure -> flowOf(resultState)
        ResultState.Loading -> flowOf(ResultState.Loading)
    }
}

fun <T, R> Flow<ResultState<T>>.flatMapLatestSuccessSuspend(
    transform: suspend (T) -> Flow<ResultState<R>>
): Flow<ResultState<R>> = flatMapLatest { resultState ->
    when (resultState) {
        is ResultState.Success -> transform(resultState.value)
        is ResultState.Failure -> flowOf(resultState)
        ResultState.Loading -> flowOf(ResultState.Loading)
    }
}

fun <A, B, C> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
): Flow<ResultState<CombinedResult3<A, B, C>>> = combineResultStateFlows(
    flowA, flowB, flowC
) { a, b, c ->
    CombinedResult3(a, b, c)
}

fun <A, B, C, K> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    transform: suspend (A, B, C) -> K
): Flow<ResultState<K>> = flow {
    combine(flowA, flowB, flowC) { resultStateA, resultStateB, resultStateC ->
        if (resultStateA is ResultState.Loading ||
            resultStateB is ResultState.Loading ||
            resultStateC is ResultState.Loading
        ) {
            ResultState.Loading
        } else if (resultStateA is ResultState.Success &&
            resultStateB is ResultState.Success &&
            resultStateC is ResultState.Success
        ) {
            ResultState.Success(
                transform(
                    resultStateA.value,
                    resultStateB.value,
                    resultStateC.value,
                )
            )
        } else {
            ResultState.Failure(
                buildList {
                    addAll(resultStateA.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateB.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateC.failureOrNull()?.appErrors.orEmpty())
                }.toImmutableList()
            )
        }
    }.collect {
        emit(it)
    }
}

fun <A, B, C, D> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
): Flow<ResultState<CombinedResult4<A, B, C, D>>> = combineResultStateFlows(
    flowA, flowB, flowC, flowD
) { a, b, c, d ->
    CombinedResult4(a, b, c, d)
}

fun <A, B, C, D, K> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
    transform: suspend (A, B, C, D) -> K
): Flow<ResultState<K>> = flow {
    combine(flowA, flowB, flowC, flowD) { resultStateA, resultStateB, resultStateC, resultStateD ->
        if (resultStateA is ResultState.Loading ||
            resultStateB is ResultState.Loading ||
            resultStateC is ResultState.Loading ||
            resultStateD is ResultState.Loading
        ) {
            ResultState.Loading
        } else if (resultStateA is ResultState.Success &&
            resultStateB is ResultState.Success &&
            resultStateC is ResultState.Success &&
            resultStateD is ResultState.Success
        ) {
            ResultState.Success(
                transform(
                    resultStateA.value,
                    resultStateB.value,
                    resultStateC.value,
                    resultStateD.value,
                )
            )
        } else {
            ResultState.Failure(
                buildList {
                    addAll(resultStateA.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateB.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateC.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateD.failureOrNull()?.appErrors.orEmpty())
                }.toImmutableList()
            )
        }
    }.collect {
        emit(it)
    }
}

fun <A, B, C, D, E> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
    flowE: Flow<ResultState<E>>,
): Flow<ResultState<CombinedResult5<A, B, C, D, E>>> = combineResultStateFlows(
    flowA, flowB, flowC, flowD, flowE
) { a, b, c, d, e ->
    CombinedResult5(a, b, c, d, e)
}

fun <A, B, C, D, E, K> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
    flowE: Flow<ResultState<E>>,
    transform: suspend (A, B, C, D, E) -> K
): Flow<ResultState<K>> = flow {
    combine(
        flowA,
        flowB,
        flowC,
        flowD,
        flowE,
    ) { resultStateA, resultStateB, resultStateC, resultStateD, resultStateE ->
        if (resultStateA is ResultState.Loading ||
            resultStateB is ResultState.Loading ||
            resultStateC is ResultState.Loading ||
            resultStateD is ResultState.Loading ||
            resultStateE is ResultState.Loading
        ) {
            ResultState.Loading
        } else if (resultStateA is ResultState.Success &&
            resultStateB is ResultState.Success &&
            resultStateC is ResultState.Success &&
            resultStateD is ResultState.Success &&
            resultStateE is ResultState.Success
        ) {
            ResultState.Success(
                transform(
                    resultStateA.value,
                    resultStateB.value,
                    resultStateC.value,
                    resultStateD.value,
                    resultStateE.value,
                )
            )
        } else {
            ResultState.Failure(
                buildList {
                    addAll(resultStateA.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateB.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateC.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateD.failureOrNull()?.appErrors.orEmpty())
                    addAll(resultStateE.failureOrNull()?.appErrors.orEmpty())
                }.toImmutableList()
            )
        }
    }.collect {
        emit(it)
    }
}

fun <A, B, C, D, E, F> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
    flowE: Flow<ResultState<E>>,
    flowF: Flow<ResultState<F>>,
): Flow<ResultState<CombinedResult6<A, B, C, D, E, F>>> = combineResultStateFlows(
    flowA, flowB, flowC, flowD, flowE, flowF
) { a, b, c, d, e, f ->
    CombinedResult6(a, b, c, d, e, f)
}

@Suppress("UNCHECKED_CAST")
fun <A, B, C, D, E, F, K> combineResultStateFlows(
    flowA: Flow<ResultState<A>>,
    flowB: Flow<ResultState<B>>,
    flowC: Flow<ResultState<C>>,
    flowD: Flow<ResultState<D>>,
    flowE: Flow<ResultState<E>>,
    flowF: Flow<ResultState<F>>,
    transform: suspend (A, B, C, D, E, F) -> K
): Flow<ResultState<K>> = flow {
    combine(
        flowA,
        flowB,
        flowC,
        flowD,
        flowE,
        flowF,
    ) { results ->
        if (results.any { it is ResultState.Loading }) {
            ResultState.Loading
        } else if (results.all { it is ResultState.Success }) {
            ResultState.Success(
                transform(
                    (results[0] as ResultState.Success<A>).value,
                    (results[1] as ResultState.Success<B>).value,
                    (results[2] as ResultState.Success<C>).value,
                    (results[3] as ResultState.Success<D>).value,
                    (results[4] as ResultState.Success<E>).value,
                    (results[5] as ResultState.Success<F>).value,
                )
            )
        } else {
            ResultState.Failure(
                results
                    .mapNotNull { it.failureOrNull()?.appErrors }
                    .flatten()
                    .toImmutableList()
            )
        }
    }.collect {
        emit(it)
    }
}

private fun <T> ResultState<T>.getOrCollectError(errors: MutableList<AppError>): T? {
    return when (this) {
        is ResultState.Success -> value
        is ResultState.Failure -> {
            errors.addAll(appErrors)
            null
        }

        ResultState.Loading -> null
    }
}

data class CombinedResult2<A, B>(val first: A, val second: B)
data class CombinedResult3<A, B, C>(val first: A, val second: B, val third: C)
data class CombinedResult4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
data class CombinedResult5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
)

data class CombinedResult6<A, B, C, D, E, F>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
)

inline fun <reified T> List<Flow<T>>.combineAll(): Flow<List<T>> {
    return if (isEmpty()) {
        flowOf(emptyList())
    } else {
        combine(this) {
            it.toList()
        }
    }
}

inline fun <reified T, R> List<Flow<T>>.combineAll(crossinline transform: (List<T>) -> R): Flow<R> {
    return if (isEmpty()) {
        flowOf(transform(emptyList()))
    } else {
        combine(this) {
            transform(it.toList())
        }
    }
}

inline fun <reified T> List<Flow<ResultState<T>>>.combineAllResultStates(): Flow<ResultState<List<T>>> {
    return combineAll().map { it.combineResultStates() }
}
