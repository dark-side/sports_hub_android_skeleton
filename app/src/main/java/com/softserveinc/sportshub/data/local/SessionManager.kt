package com.softserveinc.sportshub.data.local

import android.content.Context
import android.content.SharedPreferences
import com.softserveinc.sportshub.domain.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow(loadUser())
    val currentUser: StateFlow<UserModel?> = _currentUser.asStateFlow()

    val isLoggedIn: Boolean
        get() = _currentUser.value != null

    fun saveUser(user: UserModel) {
        prefs.edit {
            putLong(KEY_USER_ID, user.id)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_FIRST_NAME, user.firstName)
            putString(KEY_USER_LAST_NAME, user.lastName)
            putString(KEY_USER_TOKEN, user.token)
        }
        _currentUser.value = user
    }

    fun clearSession() {
        prefs.edit { clear() }
        _currentUser.value = null
    }

    fun getToken(): String? {
        return prefs.getString(KEY_USER_TOKEN, null)
    }

    private fun loadUser(): UserModel? {
        val id = prefs.getLong(KEY_USER_ID, -1)
        val email = prefs.getString(KEY_USER_EMAIL, null)
        val token = prefs.getString(KEY_USER_TOKEN, null)

        return if (id != -1L && email != null && token != null) {
            UserModel(
                id = id,
                email = email,
                firstName = prefs.getString(KEY_USER_FIRST_NAME, "") ?: "",
                lastName = prefs.getString(KEY_USER_LAST_NAME, "") ?: "",
                token = token,
            )
        } else {
            null
        }
    }

    companion object {
        private const val PREFS_NAME = "sports_hub_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_FIRST_NAME = "user_first_name"
        private const val KEY_USER_LAST_NAME = "user_last_name"
        private const val KEY_USER_TOKEN = "user_token"
    }
}
