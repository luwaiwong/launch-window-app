package com.nominal.data.models

import com.google.gson.annotations.SerializedName

data class RocketConfiguration(
    val id: Int,
    val name: String,
    val family: String?,
    @SerializedName("full_name")
    val fullName: String?,
    val variant: String?,
    val description: String?,
    @SerializedName("min_stage")
    val minStage: Int?,
    @SerializedName("max_stage")
    val maxStage: Int?,
    val length: Double?,
    val diameter: Double?,
    @SerializedName("maiden_flight")
    val maidenFlight: String?,
    @SerializedName("launch_mass")
    val launchMass: Int?,
    @SerializedName("leo_capacity")
    val leoCapacity: Int?,
    @SerializedName("gto_capacity")
    val gtoCapacity: Int?,
    @SerializedName("to_thrust")
    val toThrust: Int?,
    val apogee: Int?,
    @SerializedName("vehicle_range")
    val vehicleRange: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("info_url")
    val infoUrl: String?,
    @SerializedName("wiki_url")
    val wikiUrl: String?
)

data class Rocket(
    val id: Int,
    val configuration: RocketConfiguration
)
