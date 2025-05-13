package com.softserveinc.sportshub.domain.model.common

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UiStateWrapper<T>(
    val value: T? = null,
    val isLoading: Boolean = false,
    val errors: ImmutableList<AppError> = persistentListOf(),
) {

    companion object {

        fun <T> merge(
            accumulator: UiStateWrapper<T>?,
            value: ResultState<T>,
        ): UiStateWrapper<T> {
            return UiStateWrapper(
                value = when (value) {
                    is ResultState.Success -> {
                        value.value
                    }

                    is ResultState.Failure -> {
                        accumulator?.value
                    }

                    ResultState.Loading -> {
                        accumulator?.value
                    }
                },
                isLoading = value.isLoading,
                errors = value.failureOrNull()?.appErrors ?: persistentListOf(),
            )
        }

        fun <T> of(
            value: T,
        ): UiStateWrapper<T> {
            return UiStateWrapper(value = value)
        }
    }
}

val UiStateWrapper<*>.isEmptyAndLoading: Boolean
    get() {
        return value == null && isLoading
    }
