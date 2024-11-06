package com.softserveinc.sportshub.di.hilt

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.softserveinc.sportshub.BuildConfig
import com.softserveinc.sportshub.data.SportsHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .also { builder ->
                if (BuildConfig.DEBUG) {
                    builder.addInterceptor(
                        HttpLoggingInterceptor().also { interceptor ->
                            interceptor.level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideSportsHubService(retrofit: Retrofit): SportsHubService {
        return retrofit.create(SportsHubService::class.java)
    }
}
