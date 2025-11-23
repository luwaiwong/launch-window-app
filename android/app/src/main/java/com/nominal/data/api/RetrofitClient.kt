package com.nominal.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val spaceDevsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SpaceDevsApi.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val spaceflightNewsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SpaceflightNewsApi.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val spaceDevsApi: SpaceDevsApi by lazy {
        spaceDevsRetrofit.create(SpaceDevsApi::class.java)
    }

    val spaceflightNewsApi: SpaceflightNewsApi by lazy {
        spaceflightNewsRetrofit.create(SpaceflightNewsApi::class.java)
    }
}
