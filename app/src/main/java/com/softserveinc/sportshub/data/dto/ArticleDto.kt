package com.softserveinc.sportshub.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("description")
    val description: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("article_likes")
    val articleLikes: Int,
    @SerialName("article_dislikes")
    val articleDislikes: Int,
    @SerialName("comments_content")
    val commentsContent: List<String>,
    @SerialName("comments_count")
    val commentsCount: Int,
)
