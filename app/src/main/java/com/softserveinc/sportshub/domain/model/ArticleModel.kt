package com.softserveinc.sportshub.domain.model

import kotlinx.datetime.Instant

data class ArticleModel(
    val id: Long,
    val title: String,
    val shortDescription: String,
    val description: String,
    val authorId: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val imageUrl: String,
    val articleLikes: Int,
    val articleDislikes: Int,
    val commentsContent: String,
    val commentsCount: Int,
)
