package com.softserveinc.sportshub.data.api

import com.skydoves.sandwich.ApiResponse
import com.softserveinc.sportshub.data.dto.ArticleDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class SportsHubService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun getArticles(): ApiResponse<List<ArticleDto>> {
        return try {
            val response = httpClient.get("articles")
            ApiResponse.Success(response.body())
        } catch (e: Exception) {
            ApiResponse.error(e)
        }
    }
}
