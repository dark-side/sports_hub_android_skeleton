package com.softserveinc.sportshub

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.UiStateWrapper
import com.softserveinc.sportshub.ui.article.ArticleDetailScreen
import com.softserveinc.sportshub.ui.article.ArticleDetailViewModel
import com.softserveinc.sportshub.ui.theme.SportsHubTheme
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

@PreviewTest
@Preview(showBackground = true)
@Composable
fun ArticleDetailScreenPreview() {
    SportsHubTheme {
        ArticleDetailScreen(
            uiState = ArticleDetailViewModel.UiState(
                article = UiStateWrapper(
                    value = ArticleModel(
                        id = 1,
                        title = "Champions League Final Preview",
                        shortDescription = "A look ahead to the biggest match in European football.",
                        description = "The Champions League final is set to be an exciting clash between two of Europe's elite clubs. Both teams have shown incredible form throughout the tournament, and fans around the world are eagerly anticipating what promises to be a thrilling encounter.",
                        createdAt = Instant.parse("2026-03-20T10:00:00Z"),
                        updatedAt = Instant.parse("2026-03-20T12:00:00Z"),
                        imageUrl = "",
                        articleLikes = 42,
                        articleDislikes = 3,
                        commentsContent = persistentListOf(
                            "Great article!",
                            "Can't wait for the match!"
                        ),
                        commentsCount = 2,
                    ),
                ),
            ),
            onBack = {},
        )
    }
}
