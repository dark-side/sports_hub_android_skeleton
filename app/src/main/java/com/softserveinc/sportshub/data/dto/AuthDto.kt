package com.softserveinc.sportshub.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestWrapper(
    @SerialName("user")
    val user: LoginRequest,
)

@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class LoginResponse(
    @SerialName("authentication_token")
    val authenticationToken: String?,
    @SerialName("user")
    val user: UserDto,
)

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
)

@Serializable
data class SignUpRequestWrapper(
    @SerialName("user")
    val user: SignUpRequest,
)

@Serializable
data class SignUpRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirmation")
    val passwordConfirmation: String,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
)

@Serializable
data class SignUpResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String,
)
