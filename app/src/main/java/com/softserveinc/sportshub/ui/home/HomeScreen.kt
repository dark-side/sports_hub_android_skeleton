package com.softserveinc.sportshub.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.ui.theme.SportsHubTheme

@Preview
@Composable
fun HomeScreenPreview() {
    SportsHubTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onArticleClick: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        onArticleClick = onArticleClick,
    )
}

// TODO: GenAI: Troubleshoot frequent recompositions
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeViewModel.UiState,
    onArticleClick: (Long) -> Unit = {},
) {
    val articles = uiState.articles.value

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (articles != null) {
            items(articles) { article ->
                Article(
                    modifier = Modifier.fillMaxWidth(),
                    articleModel = article,
                    onClick = { onArticleClick(article.id) },
                )
            }
        }
    }
}

@Composable
fun Article(
    modifier: Modifier = Modifier,
    articleModel: ArticleModel,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = articleModel.imageUrl
                .toUri()
                .buildUpon()
                .encodedAuthority("10.0.2.2:3002")
                .build()
                .toString(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = articleModel.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = articleModel.shortDescription,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
