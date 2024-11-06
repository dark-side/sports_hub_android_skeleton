package com.softserveinc.sportshub.di.hilt

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.data.repository.ArticleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindArticleRepository(
        articleRepository: ArticleRepositoryImpl,
    ): ArticleRepository
}
