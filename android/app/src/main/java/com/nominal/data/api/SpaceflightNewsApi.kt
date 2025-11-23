package com.nominal.data.api

import com.nominal.data.models.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceflightNewsApi {

    @GET("articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArticlesResponse

    companion object {
        const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"
    }
}
