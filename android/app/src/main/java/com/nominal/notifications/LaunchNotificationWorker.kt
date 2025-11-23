package com.nominal.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nominal.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val launchId = inputData.getString("launch_id") ?: return@withContext Result.failure()
            val timeBeforeLaunch = inputData.getString("time_before") ?: return@withContext Result.failure()

            // Fetch the latest launch data to ensure it's still valid
            val launches = RetrofitClient.spaceDevsApi.getUpcomingLaunches(limit = 100)
            val launch = launches.results.find { it.id == launchId }

            if (launch != null) {
                // Show notification
                NotificationHelper.showLaunchNotification(
                    applicationContext,
                    launch,
                    timeBeforeLaunch
                )
                Result.success()
            } else {
                // Launch no longer exists or was scrubbed
                Result.success()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
