package com.softserveinc.sportshub.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import com.softserveinc.sportshub.domain.model.common.UiStateWrapper
import com.softserveinc.sportshub.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            return
        }

        loginUseCase(state.email, state.password)
            .onEach { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        loginResult = UiStateWrapper.merge(currentState.loginResult, result)
                    )
                }
                if (result is ResultState.Success) {
                    _events.emit(Event.LoginSuccess)
                }
            }
            .launchIn(viewModelScope)
    }

    data class UiState(
        val email: String = "",
        val password: String = "",
        val loginResult: UiStateWrapper<UserModel> = UiStateWrapper(),
    ) {
        val isLoading: Boolean get() = loginResult.isLoading
        val error: String?
            get() = loginResult.errors.firstOrNull()?.toUiString()?.let {
                when (it) {
                    is com.softserveinc.sportshub.domain.model.common.UiString.RawUiString -> it.value
                    else -> null
                }
            }
    }

    sealed interface Event {
        data object LoginSuccess : Event
    }
}
