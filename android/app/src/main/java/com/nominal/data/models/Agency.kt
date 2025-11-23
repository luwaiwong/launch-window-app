package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class Agency(
    val id: Int,
    val name: String,
    val type: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    val abbrev: String?,
    val description: String?,
    val administrator: String?,
    @SerializedName("founding_year")
    val foundingYear: String?,
    val launchers: String?,
    val spacecraft: String?,
    @SerializedName("logo_url")
    val logoUrl: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nation_url")
    val nationUrl: String?
)
