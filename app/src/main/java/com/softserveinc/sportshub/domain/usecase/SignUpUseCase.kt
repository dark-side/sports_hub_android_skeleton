package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.AuthRepository
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Flow<ResultState<UserModel>> {
        return authRepository.signUp(email, password, firstName, lastName)
    }
}
