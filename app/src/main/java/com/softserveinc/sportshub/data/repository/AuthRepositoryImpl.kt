package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.data.api.SportsHubService
import com.softserveinc.sportshub.data.dto.LoginRequest
import com.softserveinc.sportshub.data.dto.LoginRequestWrapper
import com.softserveinc.sportshub.data.dto.SignUpRequest
import com.softserveinc.sportshub.data.dto.SignUpRequestWrapper
import com.softserveinc.sportshub.data.local.SessionManager
import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.AppError
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sportsHubService: SportsHubService,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override val currentUser: StateFlow<UserModel?>
        get() = sessionManager.currentUser

    override fun login(email: String, password: String): Flow<ResultState<UserModel>> {
        return flow {
            emit(ResultState.Loading)
            emit(
                try {
                    val response = sportsHubService.login(
                        LoginRequestWrapper(
                            user = LoginRequest(
                                email = email,
                                password = password,
                            )
                        )
                    )
                    val token = response.authenticationToken
                    if (token.isNullOrEmpty()) {
                        ResultState.failure(
                            AppError.DefaultGenericRawAppError("Invalid credentials")
                        )
                    } else {
                        val user = UserModel(
                            id = response.user.id,
                            email = response.user.email,
                            firstName = "",
                            lastName = "",
                            token = token,
                        )
                        sessionManager.saveUser(user)
                        ResultState.Success(user)
                    }
                } catch (e: Exception) {
                    ResultState.failure(
                        AppError.DefaultGenericRawAppError(
                            e.message ?: "Login failed"
                        )
                    )
                }
            )
        }
    }

    override fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Flow<ResultState<UserModel>> {
        return flow {
            emit(ResultState.Loading)
            emit(
                try {
                    val response = sportsHubService.signUp(
                        SignUpRequestWrapper(
                            user = SignUpRequest(
                                email = email,
                                password = password,
                                passwordConfirmation = password,
                                firstName = firstName.ifBlank { null },
                                lastName = lastName.ifBlank { null },
                            )
                        )
                    )
                    val user = UserModel(
                        id = response.id,
                        email = response.email,
                        firstName = firstName,
                        lastName = lastName,
                        token = "",
                    )
                    ResultState.Success(user)
                } catch (e: Exception) {
                    ResultState.failure(
                        AppError.DefaultGenericRawAppError(
                            e.message ?: "Sign up failed"
                        )
                    )
                }
            )
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }
}
