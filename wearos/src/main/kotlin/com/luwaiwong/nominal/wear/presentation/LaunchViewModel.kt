package com.luwaiwong.nominal.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luwaiwong.nominal.wear.data.Launch
import com.luwaiwong.nominal.wear.data.LaunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing launch data
 */
class LaunchViewModel(
    private val repository: LaunchRepository = LaunchRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Loading)
    val uiState: StateFlow<LaunchUiState> = _uiState.asStateFlow()

    private val _selectedLaunch = MutableStateFlow<Launch?>(null)
    val selectedLaunch: StateFlow<Launch?> = _selectedLaunch.asStateFlow()

    init {
        loadUpcomingLaunches()
    }

    /**
     * Load upcoming launches
     */
    fun loadUpcomingLaunches() {
        viewModelScope.launch {
            _uiState.value = LaunchUiState.Loading
            repository.getUpcomingLaunches(limit = 20).collect { result ->
                _uiState.value = when {
                    result.isSuccess -> {
                        val launches = result.getOrNull() ?: emptyList()
                        if (launches.isEmpty()) {
                            LaunchUiState.Empty
                        } else {
                            LaunchUiState.Success(launches)
                        }
                    }
                    result.isFailure -> {
                        LaunchUiState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error"
                        )
                    }
                    else -> LaunchUiState.Error("Unknown error")
                }
            }
        }
    }

    /**
     * Load previous launches
     */
    fun loadPreviousLaunches() {
        viewModelScope.launch {
            _uiState.value = LaunchUiState.Loading
            repository.getPreviousLaunches(limit = 20).collect { result ->
                _uiState.value = when {
                    result.isSuccess -> {
                        val launches = result.getOrNull() ?: emptyList()
                        if (launches.isEmpty()) {
                            LaunchUiState.Empty
                        } else {
                            LaunchUiState.Success(launches)
                        }
                    }
                    result.isFailure -> {
                        LaunchUiState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error"
                        )
                    }
                    else -> LaunchUiState.Error("Unknown error")
                }
            }
        }
    }

    /**
     * Select a launch for detail view
     */
    fun selectLaunch(launch: Launch) {
        _selectedLaunch.value = launch
    }

    /**
     * Clear selected launch
     */
    fun clearSelectedLaunch() {
        _selectedLaunch.value = null
    }

    /**
     * Refresh data
     */
    fun refresh() {
        loadUpcomingLaunches()
    }
}

/**
 * UI state for launch data
 */
sealed class LaunchUiState {
    object Loading : LaunchUiState()
    data class Success(val launches: List<Launch>) : LaunchUiState()
    data class Error(val message: String) : LaunchUiState()
    object Empty : LaunchUiState()
}
