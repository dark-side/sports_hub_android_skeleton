package com.softserveinc.sportshub.ui.navigation

/**
 * Navigation keys for Nav3 back stack.
 * Each key represents a unique destination in the app.
 */
sealed interface NavKey {
    /** Main content with home feed */
    data object Home : NavKey

    /** Login screen */
    data object Login : NavKey

    /** Sign up screen */
    data object SignUp : NavKey

    /** Article detail screen */
    data class ArticleDetail(val articleId: Long) : NavKey
}
