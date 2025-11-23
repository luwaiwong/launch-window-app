package com.nominal.data.repository

import android.content.Context
import com.google.gson.Gson
import com.nominal.data.api.RetrofitClient
import com.nominal.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File

class NominalRepository(private val context: Context) {

    private val spaceDevsApi = RetrofitClient.spaceDevsApi
    private val spaceflightNewsApi = RetrofitClient.spaceflightNewsApi
    private val gson = Gson()

    companion object {
        private const val CACHE_EXPIRY_MS = 25 * 60 * 1000L // 25 minutes
        private const val CACHE_FILE_NAME = "nominal_cache.json"
    }

    /**
     * Fetches all data from APIs or cache
     */
    suspend fun fetchAllData(forceRefresh: Boolean = false): Result<CachedData> = withContext(Dispatchers.IO) {
        try {
            val cachedData = loadCachedData()
            val now = System.currentTimeMillis()

            // Check if cache is valid
            if (!forceRefresh && cachedData != null && (now - cachedData.lastCall) < CACHE_EXPIRY_MS) {
                return@withContext Result.success(cachedData)
            }

            // Fetch fresh data in parallel
            val launchesDeferred = async { fetchLaunches() }
            val eventsDeferred = async { fetchEvents() }
            val articlesDeferred = async { fetchArticles() }

            val launchData = launchesDeferred.await()
            val eventData = eventsDeferred.await()
            val articles = articlesDeferred.await()

            val newData = CachedData(
                launches = launchData,
                events = eventData,
                articles = articles,
                lastCall = now
            )

            // Save to cache
            saveCachedData(newData)

            Result.success(newData)
        } catch (e: Exception) {
            e.printStackTrace()
            // Try to return cached data on error
            val cachedData = loadCachedData()
            if (cachedData != null) {
                Result.success(cachedData)
            } else {
                Result.failure(e)
            }
        }
    }

    private suspend fun fetchLaunches(): LaunchData {
        val upcoming = spaceDevsApi.getUpcomingLaunches(limit = 100).results
        val previous = spaceDevsApi.getPreviousLaunches(limit = 10).results
        return LaunchData(upcoming, previous)
    }

    private suspend fun fetchEvents(): EventData {
        val upcoming = spaceDevsApi.getUpcomingEvents(limit = 50).results
        val previous = spaceDevsApi.getPreviousEvents(limit = 10).results
        return EventData(upcoming, previous)
    }

    private suspend fun fetchArticles(): List<Article> {
        return spaceflightNewsApi.getArticles(limit = 20).results
    }

    private fun loadCachedData(): CachedData? {
        return try {
            val file = File(context.filesDir, CACHE_FILE_NAME)
            if (file.exists()) {
                val json = file.readText()
                gson.fromJson(json, CachedData::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveCachedData(data: CachedData) {
        try {
            val file = File(context.filesDir, CACHE_FILE_NAME)
            val json = gson.toJson(data)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Clears all cached data
     */
    fun clearCache() {
        try {
            val file = File(context.filesDir, CACHE_FILE_NAME)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Gets the timestamp of the last API call
     */
    fun getLastCallTimestamp(): Long? {
        return loadCachedData()?.lastCall
    }

    /**
     * Process launch data for the "For You" feed
     * Interleaves launches and events chronologically
     */
    fun getForYouData(
        launches: LaunchData?,
        events: EventData?,
        showPastLaunches: Boolean,
        showPastEvents: Boolean
    ): List<Any> {
        val items = mutableListOf<Any>()

        if (showPastLaunches) {
            launches?.previous?.let { items.addAll(it) }
        }
        if (showPastEvents) {
            events?.previous?.let { items.addAll(it) }
        }

        launches?.upcoming?.let { items.addAll(it) }
        events?.upcoming?.let { items.addAll(it) }

        // Sort by date
        return items.sortedBy { item ->
            when (item) {
                is Launch -> item.net
                is Event -> item.date
                else -> ""
            }
        }
    }

    /**
     * Get highlight launch for dashboard (first upcoming)
     */
    fun getDashboardHighlightLaunch(launches: LaunchData?): Launch? {
        return launches?.upcoming?.firstOrNull()
    }

    /**
     * Get recent launches for dashboard carousel
     */
    fun getDashboardRecentLaunches(launches: LaunchData?): List<Launch> {
        return launches?.previous?.take(3) ?: emptyList()
    }

    /**
     * Get filtered upcoming launches for dashboard (skip first)
     */
    fun getDashboardFilteredLaunches(launches: LaunchData?): List<Launch> {
        return launches?.upcoming?.drop(1)?.take(3) ?: emptyList()
    }

    /**
     * Get events for dashboard
     */
    fun getDashboardEvents(events: EventData?): List<Event> {
        return events?.upcoming?.take(3) ?: emptyList()
    }

    /**
     * Get articles for dashboard
     */
    fun getDashboardArticles(articles: List<Article>?): List<Article> {
        return articles?.take(3) ?: emptyList()
    }
}
