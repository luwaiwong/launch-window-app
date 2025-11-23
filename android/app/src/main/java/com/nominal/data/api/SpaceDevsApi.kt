package com.nominal.data.api

import com.nominal.data.models.EventsResponse
import com.nominal.data.models.LaunchesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceDevsApi {

    @GET("launch/upcoming/")
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): LaunchesResponse

    @GET("launch/previous/")
    suspend fun getPreviousLaunches(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): LaunchesResponse

    @GET("event/upcoming/")
    suspend fun getUpcomingEvents(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): EventsResponse

    @GET("event/previous/")
    suspend fun getPreviousEvents(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): EventsResponse

    companion object {
        const val BASE_URL = "https://ll.thespacedevs.com/2.2.0/"
    }
}
