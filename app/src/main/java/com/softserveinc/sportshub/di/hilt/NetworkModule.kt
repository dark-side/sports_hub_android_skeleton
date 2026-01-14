package com.softserveinc.sportshub.di.hilt

import com.softserveinc.sportshub.BuildConfig
import com.softserveinc.sportshub.data.api.SportsHubService
import com.softserveinc.sportshub.data.api.createSportsHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            engine {
                connectTimeout = 60_000
                socketTimeout = 60_000
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideKtorfit(httpClient: HttpClient): Ktorfit {
        return Ktorfit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .httpClient(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideSportsHubService(ktorfit: Ktorfit): SportsHubService {
        return ktorfit.createSportsHubService()
    }
}
