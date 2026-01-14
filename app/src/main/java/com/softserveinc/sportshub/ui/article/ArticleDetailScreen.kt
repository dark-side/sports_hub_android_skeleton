@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.softserveinc.sportshub.ui.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.softserveinc.sportshub.domain.model.ArticleModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Composable
fun ArticleDetailScreen(
    articleId: Long,
    modifier: Modifier = Modifier,
    viewModel: ArticleDetailViewModel = hiltViewModel<ArticleDetailViewModel, ArticleDetailViewModel.Factory>(
        key = "article_$articleId",
        creationCallback = { factory -> factory.create(articleId) }
    ),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    ArticleDetailScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
    )
}

@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    uiState: ArticleDetailViewModel.UiState,
    onBack: () -> Unit,
) {
    val article = uiState.article.value

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Article") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                uiState.article.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.article.errors.isNotEmpty() -> {
                    Text(
                        text = "Failed to load article",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                article != null -> {
                    ArticleDetailContent(
                        modifier = Modifier.fillMaxSize(),
                        article = article,
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleDetailContent(
    modifier: Modifier = Modifier,
    article: ArticleModel,
) {
    val formattedDate = remember(article.createdAt) {
        formatPublishedDate(article.createdAt)
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(
            model = article.imageUrl
                .toUri()
                .buildUpon()
                .encodedAuthority("10.0.2.2:3002")
                .build()
                .toString(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Likes",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = article.articleLikes.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Dislikes",
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        text = article.articleDislikes.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (article.commentsContent.isNotEmpty()) {
                Text(
                    text = "Comments (${article.commentsCount})",
                    style = MaterialTheme.typography.titleMedium,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    article.commentsContent.forEach { comment ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            Text(
                                text = comment,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatPublishedDate(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${localDateTime.day}, ${localDateTime.year}"
}
