package com.nominal.notifications

import android.content.Context
import androidx.work.*
import com.nominal.data.models.Event
import com.nominal.data.models.Launch
import com.nominal.data.repository.UserSettings
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val LAUNCH_WORK_PREFIX = "launch_notif_"
    private const val EVENT_WORK_PREFIX = "event_notif_"

    /**
     * Schedule all notifications for a list of launches based on user settings
     */
    fun scheduleLaunchNotifications(
        context: Context,
        launches: List<Launch>,
        settings: UserSettings
    ) {
        if (!settings.enableNotifications) {
            cancelAllLaunchNotifications(context)
            return
        }

        // Cancel all existing launch notifications first
        cancelAllLaunchNotifications(context)

        // Schedule new notifications for each launch
        launches.forEach { launch ->
            scheduleLaunchNotification(context, launch, settings)
        }
    }

    /**
     * Schedule all notifications for a list of events based on user settings
     */
    fun scheduleEventNotifications(
        context: Context,
        events: List<Event>,
        settings: UserSettings
    ) {
        if (!settings.enableNotifications) {
            cancelAllEventNotifications(context)
            return
        }

        // Cancel all existing event notifications first
        cancelAllEventNotifications(context)

        // Schedule new notifications for each event
        events.forEach { event ->
            scheduleEventNotification(context, event, settings)
        }
    }

    private fun scheduleLaunchNotification(
        context: Context,
        launch: Launch,
        settings: UserSettings
    ) {
        try {
            val launchTime = ZonedDateTime.parse(launch.net)
            val now = ZonedDateTime.now()

            // Only schedule for future launches
            if (launchTime.isBefore(now)) return

            // Schedule for each enabled time interval
            if (settings.notifLaunch24h) {
                scheduleNotificationAt(context, launch, launchTime, 24 * 60, "24h")
            }
            if (settings.notifLaunch12h) {
                scheduleNotificationAt(context, launch, launchTime, 12 * 60, "12h")
            }
            if (settings.notifLaunch1h) {
                scheduleNotificationAt(context, launch, launchTime, 60, "1h")
            }
            if (settings.notifLaunch30m) {
                scheduleNotificationAt(context, launch, launchTime, 30, "30m")
            }
            if (settings.notifLaunch10m) {
                scheduleNotificationAt(context, launch, launchTime, 10, "10m")
            }
            if (settings.notifLaunchAtTime) {
                scheduleNotificationAt(context, launch, launchTime, 0, "at_time")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scheduleEventNotification(
        context: Context,
        event: Event,
        settings: UserSettings
    ) {
        try {
            val eventTime = ZonedDateTime.parse(event.date)
            val now = ZonedDateTime.now()

            // Only schedule for future events
            if (eventTime.isBefore(now)) return

            // Schedule for each enabled time interval
            if (settings.notifEvent24h) {
                scheduleEventNotificationAt(context, event, eventTime, 24 * 60, "24h")
            }
            if (settings.notifEvent12h) {
                scheduleEventNotificationAt(context, event, eventTime, 12 * 60, "12h")
            }
            if (settings.notifEvent1h) {
                scheduleEventNotificationAt(context, event, eventTime, 60, "1h")
            }
            if (settings.notifEvent30m) {
                scheduleEventNotificationAt(context, event, eventTime, 30, "30m")
            }
            if (settings.notifEvent10m) {
                scheduleEventNotificationAt(context, event, eventTime, 10, "10m")
            }
            if (settings.notifEventAtTime) {
                scheduleEventNotificationAt(context, event, eventTime, 0, "at_time")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scheduleNotificationAt(
        context: Context,
        launch: Launch,
        launchTime: ZonedDateTime,
        minutesBefore: Long,
        timeKey: String
    ) {
        val notificationTime = launchTime.minusMinutes(minutesBefore)
        val now = ZonedDateTime.now()

        // Only schedule if notification time is in the future
        if (notificationTime.isAfter(now)) {
            val delay = Duration.between(now, notificationTime).toMillis()

            val workName = "$LAUNCH_WORK_PREFIX${launch.id}_$timeKey"

            val data = workDataOf(
                "launch_id" to launch.id,
                "time_before" to timeKey
            )

            val workRequest = OneTimeWorkRequestBuilder<LaunchNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("launch_notifications")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    private fun scheduleEventNotificationAt(
        context: Context,
        event: Event,
        eventTime: ZonedDateTime,
        minutesBefore: Long,
        timeKey: String
    ) {
        val notificationTime = eventTime.minusMinutes(minutesBefore)
        val now = ZonedDateTime.now()

        // Only schedule if notification time is in the future
        if (notificationTime.isAfter(now)) {
            val delay = Duration.between(now, notificationTime).toMillis()

            val workName = "$EVENT_WORK_PREFIX${event.id}_$timeKey"

            val data = workDataOf(
                "event_id" to event.id,
                "time_before" to timeKey
            )

            val workRequest = OneTimeWorkRequestBuilder<EventNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("event_notifications")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    /**
     * Cancel all launch notifications
     */
    fun cancelAllLaunchNotifications(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("launch_notifications")
    }

    /**
     * Cancel all event notifications
     */
    fun cancelAllEventNotifications(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("event_notifications")
    }

    /**
     * Cancel all notifications (launches and events)
     */
    fun cancelAllNotifications(context: Context) {
        cancelAllLaunchNotifications(context)
        cancelAllEventNotifications(context)
    }

    /**
     * Get count of scheduled notifications (for developer mode)
     */
    suspend fun getScheduledNotificationCount(context: Context): Int {
        val launchWorks = WorkManager.getInstance(context)
            .getWorkInfosByTag("launch_notifications")
            .await()

        val eventWorks = WorkManager.getInstance(context)
            .getWorkInfosByTag("event_notifications")
            .await()

        return launchWorks.filter { !it.state.isFinished }.size +
                eventWorks.filter { !it.state.isFinished }.size
    }
}
