package com.softserveinc.sportshub.di.hilt

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.domain.usecase.GetArticlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetArticlesUseCase(
        articleRepository: ArticleRepository,
    ): GetArticlesUseCase {
        return GetArticlesUseCase(
            articleRepository = articleRepository,
        )
    }
}
