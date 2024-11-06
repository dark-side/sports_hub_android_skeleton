package com.softserveinc.sportshub.domain.model.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface AppError {

    @get:StringRes
    val localizedMessage: Int?

    val payload: Map<String, String>

    fun toUiString(): UiString

    data class DefaultGenericAppError(
        @get:StringRes
        override val localizedMessage: Int,
    ) : AppError {
        override val payload: Map<String, String>
            get() = emptyMap()

        override fun toUiString(): UiString {
            return UiString.LocalizedUiString(
                localizedMessage = localizedMessage,
                args = payload.values.toList(),
            )
        }
    }

    data class DefaultGenericRawAppError(
        val error: String,
    ) : AppError {

        @get:StringRes
        override val localizedMessage: Int?
            get() = null
        override val payload: Map<String, String>
            get() = emptyMap()

        override fun toUiString(): UiString {
            return UiString.RawUiString(error)
        }
    }

    data object GenericConnectivityAppError : AppError {
        @get:StringRes
        override val localizedMessage: Int
            get() = TODO("Not yet implemented")
        override val payload: Map<String, String>
            get() = TODO("Not yet implemented")

        override fun toUiString(): UiString {
            TODO("Not yet implemented")
        }
    }

    data object NoInternetConnectionAppError : AppError {
        @get:StringRes
        override val localizedMessage: Int
            get() = TODO("Not yet implemented")
        override val payload: Map<String, String>
            get() = TODO("Not yet implemented")

        override fun toUiString(): UiString {
            TODO("Not yet implemented")
        }
    }

    data object TimeoutAppError : AppError {
        @get:StringRes
        override val localizedMessage: Int
            get() = TODO("Not yet implemented")
        override val payload: Map<String, String>
            get() = TODO("Not yet implemented")

        override fun toUiString(): UiString {
            TODO("Not yet implemented")
        }
    }

    data object ServerUnreachableAppError : AppError {
        @get:StringRes
        override val localizedMessage: Int
            get() = TODO("Not yet implemented")
        override val payload: Map<String, String>
            get() = TODO("Not yet implemented")

        override fun toUiString(): UiString {
            TODO("Not yet implemented")
        }
    }

    data object UnknownAppError : AppError {
        @get:StringRes
        override val localizedMessage: Int
            get() = 0
        override val payload: Map<String, String>
            get() = emptyMap()

        override fun toUiString(): UiString {
            return UiString.LocalizedUiString(
                localizedMessage = 0,
            )
        }
    }
}

sealed interface UiString {

    @Composable
    fun uiString(): String

    data class LocalizedUiString(
        val localizedMessage: Int,
        val args: List<String> = emptyList(),
    ) : UiString {

        @Composable
        override fun uiString(): String {
            return stringResource(localizedMessage, args)
        }
    }

    data class RawUiString(
        val value: String,
    ) : UiString {

        @Composable
        override fun uiString(): String {
            return value
        }
    }
}
