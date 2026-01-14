@file:OptIn(ExperimentalTime::class)

package com.softserveinc.sportshub.data.repository

import com.softserveinc.sportshub.data.api.SportsHubService
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
                    ResultState.Success(
                        articles.map {
                            ArticleModel(
                                id = it.id,
                                title = it.title,
                                shortDescription = it.shortDescription,
                                description = it.description,
                                createdAt = Instant.DISTANT_PAST,
                                updatedAt = Instant.DISTANT_PAST,
                                imageUrl = it.imageUrl,
                                articleLikes = it.articleLikes,
                                articleDislikes = it.articleDislikes,
                                commentsCount = it.commentsCount,
                                commentsContent = it.commentsContent.toImmutableList(),
                            )
                        }
                    )
                } catch (e: Exception) {
                    ResultState.failure(AppError.UnknownAppError)
                }
            )
        }
    }
}
