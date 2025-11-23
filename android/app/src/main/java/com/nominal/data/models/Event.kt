package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class Event(
    val id: Int,
    val name: String,
    val type: EventType,
    val description: String?,
    val location: String?,
    @SerializedName("news_url")
    val newsUrl: String?,
    @SerializedName("video_url")
    val videoUrl: String?,
    @SerializedName("feature_image")
    val featureImage: String?,
    val date: String, // ISO 8601
    @SerializedName("date_precision")
    val datePrecision: TimePrecision?,
    val launches: List<Launch>?,
    val expeditions: List<Expedition>?,
    val spacestations: List<SpaceStation>?,
    val program: List<Program>?
)

data class EventType(
    val id: Int,
    val name: String
)

data class Expedition(
    val id: Int,
    val name: String,
    val start: String?,
    val end: String?
)

data class SpaceStation(
    val id: Int,
    val name: String,
    val status: SpaceStationStatus,
    @SerializedName("orbit")
    val orbit: String?,
    @SerializedName("image_url")
    val imageUrl: String?
)

data class SpaceStationStatus(
    val id: Int,
    val name: String
)
