package com.softserveinc.sportshub.domain.model.common

sealed class SimpleResult<out T> {

    data class Success<out T>(val value: T) : SimpleResult<T>()

    data class Failure(val appErrors: List<AppError>) : SimpleResult<Nothing>()

    companion object {

        fun failure(vararg appErrors: AppError): Failure {
            return Failure(appErrors.toList())
        }
    }
}

inline val <T> SimpleResult<T>.isSuccess: Boolean
    get() {
        return when (this) {
            is SimpleResult.Failure -> false
            is SimpleResult.Success -> true
        }
    }

fun <T> SimpleResult<T>.getOrThrow(): T {
    return when (this) {
        is SimpleResult.Success -> value
        // TODO: Refine .toString() logic
        is SimpleResult.Failure -> throw Exception(appErrors.joinToString { it.toString() })
    }
}

fun <T> SimpleResult<T>.getOrNull(): T? {
    return when (this) {
        is SimpleResult.Success -> value
        is SimpleResult.Failure -> null
    }
}

fun <R, T : R> SimpleResult<T>.getOrElse(onFailure: (failure: SimpleResult.Failure) -> R): R {
    return when (this) {
        is SimpleResult.Success -> value
        is SimpleResult.Failure -> onFailure(this)
    }
}

fun SimpleResult<Any>.failureOrNull(): SimpleResult.Failure? {
    return when (this) {
        is SimpleResult.Success -> null
        is SimpleResult.Failure -> this
    }
}

fun SimpleResult<Any>.failure(): SimpleResult.Failure {
    return failureOrNull() ?: throw IllegalStateException()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T, reified V> SimpleResult<T>.mapSuccess(
    crossinline transformer: T.() -> V,
): SimpleResult<V> {
    if (this is SimpleResult.Success<T>) {
        return SimpleResult.Success(transformer(value))
    }
    return this as SimpleResult<V>
}
