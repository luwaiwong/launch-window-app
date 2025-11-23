package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class LaunchesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Launch>
)

data class EventsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Event>
)

data class ArticlesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Article>
)

data class CachedData(
    val launches: LaunchData?,
    val events: EventData?,
    val articles: List<Article>?,
    @SerializedName("last_call")
    val lastCall: Long
)

data class LaunchData(
    val upcoming: List<Launch>,
    val previous: List<Launch>
)

data class EventData(
    val upcoming: List<Event>,
    val previous: List<Event>
)
