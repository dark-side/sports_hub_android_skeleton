package com.softserveinc.sportshub.di.hilt

import com.softserveinc.sportshub.data.repository.ArticleRepository
import com.softserveinc.sportshub.data.repository.AuthRepository
import com.softserveinc.sportshub.domain.usecase.GetArticleUseCase
import com.softserveinc.sportshub.domain.usecase.GetArticlesUseCase
import com.softserveinc.sportshub.domain.usecase.GetCurrentUserUseCase
import com.softserveinc.sportshub.domain.usecase.LoginUseCase
import com.softserveinc.sportshub.domain.usecase.LogoutUseCase
import com.softserveinc.sportshub.domain.usecase.SignUpUseCase
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

    @Singleton
    @Provides
    fun provideGetArticleUseCase(
        articleRepository: ArticleRepository,
    ): GetArticleUseCase {
        return GetArticleUseCase(
            articleRepository = articleRepository,
        )
    }

    @Singleton
    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
    ): LoginUseCase {
        return LoginUseCase(
            authRepository = authRepository,
        )
    }

    @Singleton
    @Provides
    fun provideSignUpUseCase(
        authRepository: AuthRepository,
    ): SignUpUseCase {
        return SignUpUseCase(
            authRepository = authRepository,
        )
    }

    @Singleton
    @Provides
    fun provideLogoutUseCase(
        authRepository: AuthRepository,
    ): LogoutUseCase {
        return LogoutUseCase(
            authRepository = authRepository,
        )
    }

    @Singleton
    @Provides
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository,
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(
            authRepository = authRepository,
        )
    }
}
