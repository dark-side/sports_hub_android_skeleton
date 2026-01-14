package com.softserveinc.sportshub.data.api

import com.softserveinc.sportshub.data.dto.ArticleDto
import com.softserveinc.sportshub.data.dto.LoginRequestWrapper
import com.softserveinc.sportshub.data.dto.LoginResponse
import com.softserveinc.sportshub.data.dto.SignUpRequestWrapper
import com.softserveinc.sportshub.data.dto.SignUpResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface SportsHubService {

    @GET("articles")
    suspend fun getArticles(): List<ArticleDto>

    @POST("auth/sign_in")
    suspend fun login(@Body request: LoginRequestWrapper): LoginResponse

    @POST("../users")
    suspend fun signUp(@Body request: SignUpRequestWrapper): SignUpResponse
}
