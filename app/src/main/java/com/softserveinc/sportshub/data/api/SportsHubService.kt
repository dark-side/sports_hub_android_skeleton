package com.softserveinc.sportshub.data.api

import com.skydoves.sandwich.ApiResponse
import com.softserveinc.sportshub.data.dto.ArticleDto
import retrofit2.http.GET

interface SportsHubService {

    @GET("articles")
    suspend fun getArticles(): ApiResponse<List<ArticleDto>>
}
