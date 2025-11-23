package com.nominal.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nominal.data.models.CachedData
import com.nominal.data.repository.NominalRepository
import com.nominal.data.repository.SettingsRepository
import com.nominal.data.repository.ThemeRepository
import com.nominal.data.repository.ThemeSettings
import com.nominal.data.repository.UserSettings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DataState {
    object Loading : DataState()
    data class Success(val data: CachedData) : DataState()
    data class Error(val message: String) : DataState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NominalRepository(application)
    private val settingsRepository = SettingsRepository(application)
    private val themeRepository = ThemeRepository(application)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _dataState.value = DataState.Error(
            when {
                throwable.message?.contains("429") == true ->
                    "API rate limit exceeded. Please try again later."
                throwable.message?.contains("Unable to resolve host") == true ->
                    "No internet connection. Please check your network."
                throwable.message?.contains("timeout") == true ->
                    "Request timeout. Please try again."
                else -> "Error: ${throwable.message ?: "Unknown error"}"
            }
        )
    }

    private val _dataState = MutableStateFlow<DataState>(DataState.Loading)
    val dataState: StateFlow<DataState> = _dataState.asStateFlow()

    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private val _themeSettings = MutableStateFlow(ThemeSettings())
    val themeSettings: StateFlow<ThemeSettings> = _themeSettings.asStateFlow()

    init {
        loadSettings()
        loadThemeSettings()
        loadData()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _settings.value = settings
            }
        }
    }

    private fun loadThemeSettings() {
        viewModelScope.launch {
            themeRepository.themeSettings.collect { themeSettings ->
                _themeSettings.value = themeSettings
            }
        }
    }

    fun loadData(forceRefresh: Boolean = false) {
        viewModelScope.launch(exceptionHandler) {
            try {
                _dataState.value = DataState.Loading
                val result = repository.fetchAllData(forceRefresh, _settings.value)
                _dataState.value = if (result.isSuccess) {
                    DataState.Success(result.getOrThrow())
                } else {
                    val error = result.exceptionOrNull()
                    DataState.Error(
                        when {
                            error?.message?.contains("429") == true ->
                                "API rate limit exceeded. Please try again in a few minutes."
                            error?.message?.contains("Unable to resolve host") == true ->
                                "No internet connection. Please check your network."
                            error?.message?.contains("timeout") == true ->
                                "Request timeout. Please try again."
                            else -> error?.message ?: "Unable to load data"
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _dataState.value = DataState.Error(
                    when {
                        e.message?.contains("429") == true ->
                            "API rate limit exceeded. Please try again in a few minutes."
                        e.message?.contains("Unable to resolve host") == true ->
                            "No internet connection. Please check your network."
                        e.message?.contains("timeout") == true ->
                            "Request timeout. Please try again."
                        else -> e.message ?: "Unable to load data"
                    }
                )
            }
        }
    }

    fun clearCache() {
        repository.clearCache()
        loadData(forceRefresh = true)
    }

    fun getLastCallTimestamp(): Long? {
        return repository.getLastCallTimestamp()
    }

    // Settings updates
    fun updateEnableNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateEnableNotifications(enabled)
            // Reschedule or cancel notifications based on new setting
            if (enabled) {
                repository.rescheduleNotifications(_settings.value)
            } else {
                repository.cancelAllNotifications()
            }
        }
    }

    fun updateNotifLaunch24h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunch24h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifLaunch12h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunch12h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifLaunch1h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunch1h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifLaunch30m(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunch30m(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifLaunch10m(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunch10m(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifLaunchAtTime(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifLaunchAtTime(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifEvent24h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifEvent24h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifEvent12h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifEvent12h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateNotifEvent1h(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifEvent1h(enabled)
            repository.rescheduleNotifications(_settings.value)
        }
    }

    fun updateFyShowPastLaunches(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateFyShowPastLaunches(enabled)
        }
    }

    fun updateFyShowPastEvents(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateFyShowPastEvents(enabled)
        }
    }

    fun updateDevMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateDevMode(enabled)
        }
    }

    fun getRepository(): NominalRepository {
        return repository
    }

    // Theme settings updates
    fun setUseDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            themeRepository.setUseDynamicColor(enabled)
        }
    }

    fun setCustomSeedColor(color: Int) {
        viewModelScope.launch {
            themeRepository.setCustomSeedColor(color)
        }
    }

    fun setCustomAccentColor(color: Int) {
        viewModelScope.launch {
            themeRepository.setCustomAccentColor(color)
        }
    }

    fun setCustomBackgroundColor(color: Int) {
        viewModelScope.launch {
            themeRepository.setCustomBackgroundColor(color)
        }
    }

    fun resetThemeToDefaults() {
        viewModelScope.launch {
            themeRepository.resetToDefaults()
        }
    }

    /**
     * Get count of scheduled notifications (for developer mode)
     */
    suspend fun getScheduledNotificationCount(): Int {
        return repository.getScheduledNotificationCount()
    }
}
