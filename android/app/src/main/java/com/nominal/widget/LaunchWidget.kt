package com.nominal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.nominal.MainActivity
import com.nominal.R
import com.nominal.data.api.RetrofitClient
import com.nominal.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LaunchWidget : AppWidgetProvider() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Start periodic updates using WorkManager
        LaunchWidgetUpdateWorker.schedulePeriodicUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel periodic updates
        LaunchWidgetUpdateWorker.cancelPeriodicUpdates(context)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_launch)

        // Set up click intent to open app
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_countdown, pendingIntent)

        // Fetch and display launch data
        scope.launch {
            try {
                val launches = RetrofitClient.spaceDevsApi.getUpcomingLaunches(limit = 1)
                val nextLaunch = launches.results.firstOrNull()

                if (nextLaunch != null) {
                    views.setTextViewText(R.id.widget_launch_name, nextLaunch.name)
                    views.setTextViewText(
                        R.id.widget_provider,
                        nextLaunch.launchServiceProvider.name
                    )

                    // Update countdown
                    val countdown = DateUtils.getCountdown(nextLaunch.net)
                    val countdownText = String.format(
                        "T-%02d:%02d:%02d:%02d",
                        countdown.days,
                        countdown.hours,
                        countdown.minutes,
                        countdown.seconds
                    )
                    views.setTextViewText(R.id.widget_countdown, countdownText)
                } else {
                    views.setTextViewText(R.id.widget_launch_name, "No upcoming launches")
                    views.setTextViewText(R.id.widget_provider, "")
                    views.setTextViewText(R.id.widget_countdown, "--:--:--:--")
                }

                // Update last update time
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val lastUpdate = "Updated: ${timeFormat.format(Date())}"
                views.setTextViewText(R.id.widget_last_update, lastUpdate)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
                views.setTextViewText(R.id.widget_launch_name, "Error loading data")
                views.setTextViewText(R.id.widget_countdown, "--:--:--:--")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, LaunchWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            val intent = Intent(context, LaunchWidget::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            }
            context.sendBroadcast(intent)
        }
    }
}
