@file:OptIn(ExperimentalTime::class)

package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.data.api.SportsHubService
import com.softserveinc.sportshub.data.dto.ArticleDto
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.AppError
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ArticleRepositoryImpl @Inject constructor(
    private val sportsHubService: SportsHubService,
) : ArticleRepository {

    override fun getArticles(): Flow<ResultState<List<ArticleModel>>> {
        return flow {
            emit(
                try {
                    val articles = sportsHubService.getArticles()
                    ResultState.Success(articles.map { it.toModel() })
                } catch (e: Exception) {
                    ResultState.failure(AppError.UnknownAppError)
                }
            )
        }
    }

    override fun getArticleById(id: Long): Flow<ResultState<ArticleModel>> {
        return flow {
            emit(
                try {
                    val article = sportsHubService.getArticle(id)
                    ResultState.Success(article.toModel())
                } catch (e: Exception) {
                    ResultState.failure(AppError.UnknownAppError)
                }
            )
        }
    }

    private fun ArticleDto.toModel(): ArticleModel {
        return ArticleModel(
            id = id,
            title = title,
            shortDescription = shortDescription,
            description = description,
            createdAt = Instant.parse(createdAt),
            updatedAt = Instant.parse(updatedAt),
            imageUrl = imageUrl,
            articleLikes = articleLikes,
            articleDislikes = articleDislikes,
            commentsCount = commentsCount,
            commentsContent = commentsContent.toImmutableList(),
        )
    }
}
