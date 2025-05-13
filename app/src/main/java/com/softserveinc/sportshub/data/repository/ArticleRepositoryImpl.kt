package com.softserveinc.sportshub.data.repository

import com.skydoves.sandwich.ApiResponse
import com.softserveinc.sportshub.data.api.SportsHubService
import com.softserveinc.sportshub.domain.model.ArticleModel
import com.softserveinc.sportshub.domain.model.common.AppError
import com.softserveinc.sportshub.domain.model.common.ResultState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val sportsHubService: SportsHubService,
) : ArticleRepository {

    override fun getArticles(): Flow<ResultState<List<ArticleModel>>> {
        return flow {
            emit(
                when (val response = sportsHubService.getArticles()) {
                    is ApiResponse.Success -> {
                        ResultState.Success(
                            response.data.map {
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
                    }

                    is ApiResponse.Failure.Error -> {
                        ResultState.failure(
                            // TODO: Turn payload to errorType: errorType = response.message(),
                            AppError.UnknownAppError,
                        )
                    }

                    is ApiResponse.Failure.Exception -> {
                        ResultState.failure(
                            // TODO: Turn payload to errorType: errorType = response.message(),
                            AppError.UnknownAppError,
                        )
                    }
                }
            )
        }
    }
}
