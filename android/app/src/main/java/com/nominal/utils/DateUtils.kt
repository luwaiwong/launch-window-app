package com.nominal.utils

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    data class Countdown(
        val days: Long,
        val hours: Long,
        val minutes: Long,
        val seconds: Long,
        val isPast: Boolean
    )

    fun getCountdown(isoDateString: String): Countdown {
        return try {
            val launchTime = ZonedDateTime.parse(isoDateString)
            val now = ZonedDateTime.now()
            val duration = Duration.between(now, launchTime)

            if (duration.isNegative) {
                val positiveDuration = duration.abs()
                Countdown(
                    days = positiveDuration.toDays(),
                    hours = positiveDuration.toHours() % 24,
                    minutes = positiveDuration.toMinutes() % 60,
                    seconds = positiveDuration.seconds % 60,
                    isPast = true
                )
            } else {
                Countdown(
                    days = duration.toDays(),
                    hours = duration.toHours() % 24,
                    minutes = duration.toMinutes() % 60,
                    seconds = duration.seconds % 60,
                    isPast = false
                )
            }
        } catch (e: Exception) {
            Countdown(0, 0, 0, 0, false)
        }
    }

    fun formatDate(isoDateString: String): String {
        return try {
            val dateTime = ZonedDateTime.parse(isoDateString)
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm", Locale.getDefault())
            dateTime.format(formatter)
        } catch (e: Exception) {
            isoDateString
        }
    }

    fun formatRelativeTime(isoDateString: String): String {
        return try {
            val dateTime = ZonedDateTime.parse(isoDateString)
            val now = ZonedDateTime.now()
            val duration = Duration.between(now, dateTime)

            when {
                duration.isNegative -> {
                    val absDuration = duration.abs()
                    when {
                        absDuration.toDays() > 0 -> "${absDuration.toDays()}d ago"
                        absDuration.toHours() > 0 -> "${absDuration.toHours()}h ago"
                        absDuration.toMinutes() > 0 -> "${absDuration.toMinutes()}m ago"
                        else -> "Just now"
                    }
                }
                else -> {
                    when {
                        duration.toDays() > 0 -> "in ${duration.toDays()}d"
                        duration.toHours() > 0 -> "in ${duration.toHours()}h"
                        duration.toMinutes() > 0 -> "in ${duration.toMinutes()}m"
                        else -> "Now"
                    }
                }
            }
        } catch (e: Exception) {
            ""
        }
    }
}
