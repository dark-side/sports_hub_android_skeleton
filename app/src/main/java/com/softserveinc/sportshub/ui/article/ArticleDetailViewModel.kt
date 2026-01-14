package com.softserveinc.sportshub.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import com.softserveinc.sportshub.domain.model.common.UiStateWrapper
import com.softserveinc.sportshub.domain.usecase.GetArticleUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = ArticleDetailViewModel.Factory::class)
class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted private val articleId: Long,
    getArticleUseCase: GetArticleUseCase,
) : ViewModel() {

    val uiStateFlow = getArticleUseCase(articleId)
        .map { result ->
            Signal(article = result)
        }
        .scan(UiState.initialUiState) { accumulator, value ->
            UiState(
                article = UiStateWrapper.merge(accumulator.article, value.article)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState.initialUiState,
        )

    data class Signal(
        val article: ResultState<ArticleModel>,
    )

    data class UiState(
        val article: UiStateWrapper<ArticleModel> = UiStateWrapper(),
    ) {

        companion object {

            val initialUiState = UiState(
                article = UiStateWrapper(isLoading = true),
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(articleId: Long): ArticleDetailViewModel
    }
}
