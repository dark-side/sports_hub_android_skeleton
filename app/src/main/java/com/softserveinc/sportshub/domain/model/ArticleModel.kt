@file:OptIn(ExperimentalTime::class)

package com.softserveinc.sportshub.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class ArticleModel(
    val id: Long,
    val title: String,
    val shortDescription: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val imageUrl: String,
    val articleLikes: Int,
    val articleDislikes: Int,
    val commentsContent: ImmutableList<String>,
    val commentsCount: Int,
)
