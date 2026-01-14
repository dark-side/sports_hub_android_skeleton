package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.AuthRepository
import com.softserveinc.sportshub.domain.model.UserModel
import kotlinx.coroutines.flow.StateFlow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): StateFlow<UserModel?> {
        return authRepository.currentUser
    }
}
