package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.AuthRepository
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow

class LoginUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(email: String, password: String): Flow<ResultState<UserModel>> {
        return authRepository.login(email, password)
    }
}
