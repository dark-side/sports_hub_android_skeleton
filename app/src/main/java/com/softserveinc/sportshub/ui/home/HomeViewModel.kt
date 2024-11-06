package com.softserveinc.sportshub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.SimpleResult
import com.softserveinc.sportshub.domain.usecase.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getArticlesUseCase: GetArticlesUseCase,
) : ViewModel() {

    // TODO: Handle errors, retries
    val uiStateFlow = getArticlesUseCase()
        .map { result ->
            when (result) {
                is SimpleResult.Success -> {
                    UiState(
                        articles = result.value,
                    )
                }

                is SimpleResult.Failure -> {
                    UiState()
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState(isLoading = true),
        )

    data class UiState(
        val isLoading: Boolean = false,
        val articles: List<ArticleModel> = emptyList(),
    )
}
