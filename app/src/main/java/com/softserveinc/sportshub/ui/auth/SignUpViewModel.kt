package com.softserveinc.sportshub.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import com.softserveinc.sportshub.domain.model.common.UiStateWrapper
import com.softserveinc.sportshub.domain.usecase.SignUpUseCase
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    fun onFirstNameChange(firstName: String) {
        _uiState.update { it.copy(firstName = firstName) }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update { it.copy(lastName = lastName) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onSignUpClick() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            return
        }

        signUpUseCase(
            email = state.email,
            password = state.password,
            firstName = state.firstName,
            lastName = state.lastName,
        )
            .onEach { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        signUpResult = UiStateWrapper.merge(currentState.signUpResult, result)
                    )
                }
                if (result is ResultState.Success) {
                    _events.emit(Event.SignUpSuccess)
                }
            }
            .launchIn(viewModelScope)
    }

    data class UiState(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val signUpResult: UiStateWrapper<UserModel> = UiStateWrapper(),
    ) {
        val isLoading: Boolean get() = signUpResult.isLoading
        val error: String?
            get() = signUpResult.errors.firstOrNull()?.toUiString()?.let {
                when (it) {
                    is com.softserveinc.sportshub.domain.model.common.UiString.RawUiString -> it.value
                    else -> null
                }
            }
    }

    sealed interface Event {
        data object SignUpSuccess : Event
    }
}
