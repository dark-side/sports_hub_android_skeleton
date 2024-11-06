package com.softserveinc.sportshub.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

// TODO: GenAI: Troubleshoot frequent recompositions
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeViewModel.UiState,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(uiState.articles) {
            Article(
                modifier = Modifier.fillMaxWidth(),
                articleModel = it,
            )
        }
    }
}

@Composable
fun Article(
    modifier: Modifier = Modifier,
    articleModel: ArticleModel,
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = articleModel.title,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
