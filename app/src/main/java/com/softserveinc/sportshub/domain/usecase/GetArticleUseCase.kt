package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow

class GetArticleUseCase(
    private val articleRepository: ArticleRepository,
) {

    operator fun invoke(id: Long): Flow<ResultState<ArticleModel>> {
        return articleRepository.getArticleById(id)
    }
}
