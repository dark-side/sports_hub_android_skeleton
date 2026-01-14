package com.softserveinc.sportshub.data.api

import com.softserveinc.sportshub.data.dto.ArticleDto
import com.softserveinc.sportshub.data.dto.LoginRequestWrapper
import com.softserveinc.sportshub.data.dto.LoginResponse
import com.softserveinc.sportshub.data.dto.SignUpRequestWrapper
import com.softserveinc.sportshub.data.dto.SignUpResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface SportsHubService {

    @GET("articles")
    suspend fun getArticles(): List<ArticleDto>

    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: Long): ArticleDto

    @POST("auth/sign_in")
    suspend fun login(@Body request: LoginRequestWrapper): LoginResponse

    @POST("../users")
    suspend fun signUp(@Body request: SignUpRequestWrapper): SignUpResponse
}
