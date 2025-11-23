package com.nominal.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.nominal.MainActivity
import com.nominal.NominalApplication
import com.nominal.R
import com.nominal.data.models.Launch
import com.nominal.utils.DateUtils

object NotificationHelper {

    fun showLaunchNotification(
        context: Context,
        launch: Launch,
        timeBeforeLaunch: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("launch_id", launch.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            launch.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notification = NotificationCompat.Builder(context, NominalApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getNotificationTitle(timeBeforeLaunch))
            .setContentText(launch.name)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${launch.name}\n${launch.launchServiceProvider.name}\n${DateUtils.formatDate(launch.net)}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(launch.id.hashCode(), notification)
    }

    fun showEventNotification(
        context: Context,
        eventId: Int,
        eventName: String,
        eventDate: String,
        timeBeforeLaunch: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            eventId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NominalApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getEventNotificationTitle(timeBeforeLaunch))
            .setContentText(eventName)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$eventName\n${DateUtils.formatDate(eventDate)}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(eventId, notification)
    }

    private fun getNotificationTitle(timeBeforeLaunch: String): String {
        return when (timeBeforeLaunch) {
            "at_time" -> "🚀 Launch Happening Now!"
            "10m" -> "🚀 Launch in 10 Minutes"
            "30m" -> "🚀 Launch in 30 Minutes"
            "1h" -> "🚀 Launch in 1 Hour"
            "12h" -> "🚀 Launch in 12 Hours"
            "24h" -> "🚀 Launch in 24 Hours"
            else -> "🚀 Upcoming Launch"
        }
    }

    private fun getEventNotificationTitle(timeBeforeEvent: String): String {
        return when (timeBeforeEvent) {
            "at_time" -> "📅 Event Happening Now!"
            "10m" -> "📅 Event in 10 Minutes"
            "30m" -> "📅 Event in 30 Minutes"
            "1h" -> "📅 Event in 1 Hour"
            "12h" -> "📅 Event in 12 Hours"
            "24h" -> "📅 Event in 24 Hours"
            else -> "📅 Upcoming Event"
        }
    }
}
