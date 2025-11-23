package com.luwaiwong.nominal.wear.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Service for fetching launch data from TheSpaceDevs API
 */
class LaunchApiService {
    private val baseUrl = "https://ll.thespacedevs.com/2.2.0"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Launch::class.java, LaunchDeserializer())
        .create()

    /**
     * Fetch upcoming launches
     */
    suspend fun getUpcomingLaunches(limit: Int = 10): Result<List<Launch>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/launch/upcoming/?limit=$limit")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }

                val body = response.body?.string()
                    ?: return@withContext Result.failure(Exception("Empty response"))

                val launchResponse = gson.fromJson(body, LaunchResponse::class.java)
                Result.success(launchResponse.results)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetch previous launches
     */
    suspend fun getPreviousLaunches(limit: Int = 10): Result<List<Launch>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/launch/previous/?limit=$limit")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }

                val body = response.body?.string()
                    ?: return@withContext Result.failure(Exception("Empty response"))

                val launchResponse = gson.fromJson(body, LaunchResponse::class.java)
                Result.success(launchResponse.results)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Custom deserializer for Launch to handle nested JSON
 */
class LaunchDeserializer : JsonDeserializer<Launch> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Launch {
        val obj = json.asJsonObject

        // Extract status
        val statusObj = obj.getAsJsonObject("status")
        val status = LaunchStatus(
            id = statusObj.get("id")?.asInt ?: 0,
            name = statusObj.get("name")?.asString ?: "Unknown",
            abbrev = statusObj.get("abbrev")?.asString ?: "",
            description = statusObj.get("description")?.asString
        )

        // Extract launch service provider
        val lspObj = obj.getAsJsonObject("launch_service_provider")
        val provider = LaunchProvider(
            id = lspObj.get("id")?.asInt ?: 0,
            name = lspObj.get("name")?.asString ?: "Unknown",
            type = lspObj.get("type")?.asString
        )

        // Extract rocket
        val rocketObj = obj.getAsJsonObject("rocket")
        val rocketConfigObj = rocketObj?.getAsJsonObject("configuration")
        val rocket = Rocket(
            id = rocketConfigObj?.get("id")?.asInt ?: 0,
            name = rocketConfigObj?.get("name")?.asString ?: "Unknown",
            family = rocketConfigObj?.get("family")?.asString,
            variant = rocketConfigObj?.get("variant")?.asString
        )

        // Extract pad
        val padObj = obj.getAsJsonObject("pad")
        val pad = if (padObj != null) {
            val locationObj = padObj.getAsJsonObject("location")
            LaunchPad(
                id = padObj.get("id")?.asInt ?: 0,
                name = padObj.get("name")?.asString ?: "Unknown",
                location = locationObj?.get("name")?.asString ?: "Unknown"
            )
        } else null

        // Extract mission
        val missionObj = obj.getAsJsonObject("mission")
        val mission = if (missionObj != null) {
            Mission(
                id = missionObj.get("id")?.asInt ?: 0,
                name = missionObj.get("name")?.asString ?: "Unknown",
                description = missionObj.get("description")?.asString,
                type = missionObj.get("type")?.asString
            )
        } else null

        return Launch(
            id = obj.get("id")?.asString ?: "",
            name = obj.get("name")?.asString ?: "Unknown Launch",
            net = obj.get("net")?.asString ?: "",
            status = status,
            provider = provider,
            rocket = rocket,
            pad = pad,
            mission = mission,
            image = obj.get("image")?.asString,
            webcastLive = obj.get("webcast_live")?.asBoolean ?: false
        )
    }
}
