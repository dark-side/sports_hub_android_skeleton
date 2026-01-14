package com.softserveinc.sportshub.di.hilt

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.data.repository.ArticleRepositoryImpl
import com.softserveinc.sportshub.data.repository.AuthRepository
import com.softserveinc.sportshub.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(impl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
