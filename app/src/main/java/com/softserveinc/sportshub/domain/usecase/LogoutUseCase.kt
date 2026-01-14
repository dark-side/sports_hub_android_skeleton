package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke() {
        authRepository.logout()
    }
}
