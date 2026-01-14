package com.softserveinc.sportshub.domain.model

data class UserModel(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val token: String,
) {
    val displayName: String
        get() = if (firstName.isNotBlank() || lastName.isNotBlank()) {
            "$firstName $lastName".trim()
        } else {
            email.substringBefore("@")
        }
}
