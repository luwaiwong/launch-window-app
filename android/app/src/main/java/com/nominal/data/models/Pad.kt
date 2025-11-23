package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class Location(
    val id: Int,
    val name: String,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("map_image")
    val mapImage: String?,
    @SerializedName("total_launch_count")
    val totalLaunchCount: Int?,
    @SerializedName("total_landing_count")
    val totalLandingCount: Int?
)

data class Pad(
    val id: Int,
    val name: String,
    @SerializedName("info_url")
    val infoUrl: String?,
    @SerializedName("wiki_url")
    val wikiUrl: String?,
    @SerializedName("map_url")
    val mapUrl: String?,
    val latitude: String?,
    val longitude: String?,
    val location: Location?,
    @SerializedName("map_image")
    val mapImage: String?,
    @SerializedName("total_launch_count")
    val totalLaunchCount: Int?
)
