package com.luwaiwong.nominal.wear.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * Data model for a rocket launch
 */
data class Launch(
    val id: String,
    val name: String,
    val net: String, // Next Event Time
    val status: LaunchStatus,
    val provider: LaunchProvider,
    val rocket: Rocket,
    val pad: LaunchPad?,
    val mission: Mission?,
    val image: String?,
    val webcastLive: Boolean = false
) {
    /**
     * Get formatted countdown string
     */
    fun getCountdown(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val launchDate = inputFormat.parse(net) ?: return "TBD"

            val now = Date()
            val diff = launchDate.time - now.time

            when {
                diff < 0 -> "Launched"
                diff < 60 * 1000 -> "< 1 min"
                diff < 60 * 60 * 1000 -> {
                    val minutes = (diff / (60 * 1000)).toInt()
                    "T-${minutes}m"
                }
                diff < 24 * 60 * 60 * 1000 -> {
                    val hours = (diff / (60 * 60 * 1000)).toInt()
                    "T-${hours}h"
                }
                diff < 7 * 24 * 60 * 60 * 1000 -> {
                    val days = (diff / (24 * 60 * 60 * 1000)).toInt()
                    "T-${days}d"
                }
                else -> {
                    val outputFormat = SimpleDateFormat("MMM dd", Locale.US)
                    outputFormat.format(launchDate)
                }
            }
        } catch (e: Exception) {
            "TBD"
        }
    }

    /**
     * Get formatted date and time
     */
    fun getFormattedDateTime(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(net) ?: return net

            val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.US)
            outputFormat.format(date) + " UTC"
        } catch (e: Exception) {
            net
        }
    }

    /**
     * Check if launch is upcoming (in the future)
     */
    fun isUpcoming(): Boolean {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(net) ?: return false
            date.after(Date())
        } catch (e: Exception) {
            false
        }
    }
}

data class LaunchStatus(
    val id: Int,
    val name: String,
    val abbrev: String,
    val description: String?
)

data class LaunchProvider(
    val id: Int,
    val name: String,
    val type: String?
)

data class Rocket(
    val id: Int,
    val name: String,
    val family: String?,
    val variant: String?
)

data class LaunchPad(
    val id: Int,
    val name: String,
    val location: String
)

data class Mission(
    val id: Int,
    val name: String,
    val description: String?,
    val type: String?
)

/**
 * Response from API
 */
data class LaunchResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Launch>
)
