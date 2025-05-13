package com.softserveinc.sportshub.domain.usecase

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow

class GetArticlesUseCase(
    private val articleRepository: ArticleRepository,
) {

    operator fun invoke(): Flow<ResultState<List<ArticleModel>>> {
        return articleRepository.getArticles()
    }
}
