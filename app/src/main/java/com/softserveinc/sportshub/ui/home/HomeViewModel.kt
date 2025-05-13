package com.softserveinc.sportshub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import com.softserveinc.sportshub.domain.model.common.UiStateWrapper
import com.softserveinc.sportshub.domain.model.common.mapSuccess
import com.softserveinc.sportshub.domain.usecase.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getArticlesUseCase: GetArticlesUseCase,
) : ViewModel() {

    // TODO: Handle errors, retries
    val uiStateFlow = getArticlesUseCase()
        .map { result ->
            Signal(
                articles = result.mapSuccess { it.toImmutableList() },
            )
        }
        .scan(UiState.initialUiState) { accumulator, value ->
            UiState(
                articles = UiStateWrapper.merge(accumulator.articles, value.articles)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState.initialUiState,
        )

    data class Signal(
        val articles: ResultState<ImmutableList<ArticleModel>>,
    )

    data class UiState(
        val articles: UiStateWrapper<ImmutableList<ArticleModel>> = UiStateWrapper(),
    ) {

        companion object {

            val initialUiState = UiState(
                articles = UiStateWrapper(isLoading = true),
            )
        }
    }
}
