package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getArticles(): Flow<ResultState<List<ArticleModel>>>

    fun getArticleById(id: Long): Flow<ResultState<ArticleModel>>
}
