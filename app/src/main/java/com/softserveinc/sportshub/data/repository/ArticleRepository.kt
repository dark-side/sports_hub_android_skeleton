package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.SimpleResult
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getArticles(): Flow<SimpleResult<List<ArticleModel>>>
}
