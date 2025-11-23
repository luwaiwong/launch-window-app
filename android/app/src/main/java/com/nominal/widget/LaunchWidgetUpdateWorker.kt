package com.nominal.widget

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class LaunchWidgetUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Update all widgets
            LaunchWidget.updateAllWidgets(applicationContext)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "LaunchWidgetUpdateWork"
        private const val DYNAMIC_WORK_NAME = "LaunchWidgetDynamicUpdateWork"

        fun schedulePeriodicUpdates(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val updateRequest = PeriodicWorkRequestBuilder<LaunchWidgetUpdateWorker>(
                30, // Repeat every 30 minutes
                TimeUnit.MINUTES,
                15, // With 15 minute flex period
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                updateRequest
            )
        }

        fun cancelPeriodicUpdates(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }

        /**
         * Schedule a one-time update at a specific interval (used for dynamic updates)
         * This is called when widget needs to update more frequently as launch approaches
         */
        fun scheduleNextUpdate(context: Context, intervalMinutes: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val updateRequest = OneTimeWorkRequestBuilder<LaunchWidgetUpdateWorker>()
                .setInitialDelay(intervalMinutes, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                DYNAMIC_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                updateRequest
            )
        }
    }
}
