package com.nominal.data.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class LaunchStatus(
    val id: Int,
    val name: String,
    val abbrev: String?,
    val description: String?
)

data class Mission(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("launch_designator")
    val launchDesignator: String?,
    val type: String?,
    val orbit: Orbit?
)

data class Orbit(
    val id: Int,
    val name: String,
    val abbrev: String?
)

data class Launch(
    val id: String,
    val name: String,
    val status: LaunchStatus,
    val net: String, // Network Expected Time (ISO 8601)
    @SerializedName("window_end")
    val windowEnd: String?,
    @SerializedName("window_start")
    val windowStart: String?,
    val probability: Int?,
    val holdreason: String?,
    val failreason: String?,
    @SerializedName("hashtag")
    val hashtag: String?,
    @SerializedName("launch_service_provider")
    val launchServiceProvider: Agency,
    val rocket: Rocket,
    val mission: Mission?,
    val pad: Pad,
    @SerializedName("webcast_live")
    val webcastLive: Boolean,
    val image: String?,
    @SerializedName("infographic")
    val infographic: String?,
    val program: List<Program>?,
    @SerializedName("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int?,
    @SerializedName("location_launch_attempt_count")
    val locationLaunchAttemptCount: Int?,
    @SerializedName("pad_launch_attempt_count")
    val padLaunchAttemptCount: Int?,
    @SerializedName("agency_launch_attempt_count")
    val agencyLaunchAttemptCount: Int?,
    @SerializedName("orbital_launch_attempt_count_year")
    val orbitalLaunchAttemptCountYear: Int?,
    @SerializedName("location_launch_attempt_count_year")
    val locationLaunchAttemptCountYear: Int?,
    @SerializedName("pad_launch_attempt_count_year")
    val padLaunchAttemptCountYear: Int?,
    @SerializedName("agency_launch_attempt_count_year")
    val agencyLaunchAttemptCountYear: Int?,
    @SerializedName("vid_urls")
    val vidUrls: List<VidUrl>?,
    @SerializedName("info_urls")
    val infoUrls: List<InfoUrl>?
) {
    fun getTimePrecision(): TimePrecision {
        // Parse the net string to determine precision
        // This is a simplified version - you may need to adjust based on actual API response
        return TimePrecision.MINUTE
    }

    fun isPast(): Boolean {
        return try {
            val launchTime = ZonedDateTime.parse(net)
            launchTime.isBefore(ZonedDateTime.now())
        } catch (e: Exception) {
            false
        }
    }

    fun isSuccessful(): Boolean {
        return status.id == 3 // Success
    }

    fun isFailed(): Boolean {
        return status.id == 4 // Failure
    }

    fun isPartialFailure(): Boolean {
        return status.id == 7 // Partial Failure
    }
}

data class Program(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?
)

data class VidUrl(
    val priority: Int?,
    val title: String?,
    val description: String?,
    @SerializedName("feature_image")
    val featureImage: String?,
    val url: String
)

data class InfoUrl(
    val priority: Int?,
    val title: String?,
    val description: String?,
    @SerializedName("feature_image")
    val featureImage: String?,
    val url: String
)
