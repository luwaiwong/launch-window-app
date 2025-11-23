package com.luwaiwong.nominal.wear.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for launch data
 */
class LaunchRepository(
    private val apiService: LaunchApiService = LaunchApiService()
) {
    /**
     * Get upcoming launches as a Flow
     */
    fun getUpcomingLaunches(limit: Int = 10): Flow<Result<List<Launch>>> = flow {
        emit(apiService.getUpcomingLaunches(limit))
    }

    /**
     * Get previous launches as a Flow
     */
    fun getPreviousLaunches(limit: Int = 10): Flow<Result<List<Launch>>> = flow {
        emit(apiService.getPreviousLaunches(limit))
    }

    /**
     * Get a single upcoming launch (the next one)
     */
    suspend fun getNextLaunch(): Result<Launch?> {
        return when (val result = apiService.getUpcomingLaunches(1)) {
            is Result.Success -> Result.success(result.getOrNull()?.firstOrNull())
            is Result.Failure -> result
        }
    }
}
