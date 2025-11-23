package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class Article(
    val id: Int,
    val title: String,
    val url: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("news_site")
    val newsSite: String,
    val summary: String,
    @SerializedName("published_at")
    val publishedAt: String, // ISO 8601
    @SerializedName("updated_at")
    val updatedAt: String,
    val featured: Boolean,
    val launches: List<ArticleLaunch>?,
    val events: List<ArticleEvent>?
)

data class ArticleLaunch(
    @SerializedName("launch_id")
    val launchId: String,
    val provider: String?
)

data class ArticleEvent(
    @SerializedName("event_id")
    val eventId: Int,
    val provider: String?
)
