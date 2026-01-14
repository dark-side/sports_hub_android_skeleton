package com.softserveinc.sportshub.ui

import androidx.lifecycle.ViewModel
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.usecase.GetCurrentUserUseCase
import com.softserveinc.sportshub.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    val currentUser: StateFlow<UserModel?> = getCurrentUserUseCase()

    fun logout() {
        logoutUseCase()
    }
}
