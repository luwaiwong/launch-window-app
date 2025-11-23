package com.nominal.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nominal.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val eventId = inputData.getInt("event_id", -1)
            if (eventId == -1) return@withContext Result.failure()

            val timeBeforeEvent = inputData.getString("time_before") ?: return@withContext Result.failure()

            // Fetch the latest event data
            val events = RetrofitClient.spaceDevsApi.getUpcomingEvents(limit = 50)
            val event = events.results.find { it.id == eventId }

            if (event != null) {
                // Show notification
                NotificationHelper.showEventNotification(
                    applicationContext,
                    event.id,
                    event.name,
                    event.date,
                    timeBeforeEvent
                )
                Result.success()
            } else {
                // Event no longer exists
                Result.success()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
