package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.domain.model.UserModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val currentUser: StateFlow<UserModel?>

    fun login(email: String, password: String): Flow<ResultState<UserModel>>

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Flow<ResultState<UserModel>>

    fun logout()
}
