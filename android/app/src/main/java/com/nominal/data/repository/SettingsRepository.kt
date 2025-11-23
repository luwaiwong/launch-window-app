package com.nominal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserSettings(
    val enableNotifications: Boolean = true,
    val notifLaunch24h: Boolean = true,
    val notifLaunch12h: Boolean = true,
    val notifLaunch1h: Boolean = true,
    val notifLaunch30m: Boolean = true,
    val notifLaunch10m: Boolean = true,
    val notifLaunchAtTime: Boolean = true,
    val notifEvent24h: Boolean = true,
    val notifEvent12h: Boolean = true,
    val notifEvent1h: Boolean = true,
    val notifEvent30m: Boolean = false,
    val notifEvent10m: Boolean = false,
    val notifEventAtTime: Boolean = false,
    val fyShowPastLaunches: Boolean = true,
    val fyShowPastEvents: Boolean = true,
    val devMode: Boolean = false
)

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val ENABLE_NOTIFICATIONS = booleanPreferencesKey("enable_notifications")
        val NOTIF_LAUNCH_24H = booleanPreferencesKey("notif_launch_24h")
        val NOTIF_LAUNCH_12H = booleanPreferencesKey("notif_launch_12h")
        val NOTIF_LAUNCH_1H = booleanPreferencesKey("notif_launch_1h")
        val NOTIF_LAUNCH_30M = booleanPreferencesKey("notif_launch_30m")
        val NOTIF_LAUNCH_10M = booleanPreferencesKey("notif_launch_10m")
        val NOTIF_LAUNCH_AT_TIME = booleanPreferencesKey("notif_launch_at_time")
        val NOTIF_EVENT_24H = booleanPreferencesKey("notif_event_24h")
        val NOTIF_EVENT_12H = booleanPreferencesKey("notif_event_12h")
        val NOTIF_EVENT_1H = booleanPreferencesKey("notif_event_1h")
        val NOTIF_EVENT_30M = booleanPreferencesKey("notif_event_30m")
        val NOTIF_EVENT_10M = booleanPreferencesKey("notif_event_10m")
        val NOTIF_EVENT_AT_TIME = booleanPreferencesKey("notif_event_at_time")
        val FY_SHOW_PAST_LAUNCHES = booleanPreferencesKey("fy_show_past_launches")
        val FY_SHOW_PAST_EVENTS = booleanPreferencesKey("fy_show_past_events")
        val DEV_MODE = booleanPreferencesKey("dev_mode")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            enableNotifications = preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] ?: true,
            notifLaunch24h = preferences[PreferencesKeys.NOTIF_LAUNCH_24H] ?: true,
            notifLaunch12h = preferences[PreferencesKeys.NOTIF_LAUNCH_12H] ?: true,
            notifLaunch1h = preferences[PreferencesKeys.NOTIF_LAUNCH_1H] ?: true,
            notifLaunch30m = preferences[PreferencesKeys.NOTIF_LAUNCH_30M] ?: true,
            notifLaunch10m = preferences[PreferencesKeys.NOTIF_LAUNCH_10M] ?: true,
            notifLaunchAtTime = preferences[PreferencesKeys.NOTIF_LAUNCH_AT_TIME] ?: true,
            notifEvent24h = preferences[PreferencesKeys.NOTIF_EVENT_24H] ?: true,
            notifEvent12h = preferences[PreferencesKeys.NOTIF_EVENT_12H] ?: true,
            notifEvent1h = preferences[PreferencesKeys.NOTIF_EVENT_1H] ?: true,
            notifEvent30m = preferences[PreferencesKeys.NOTIF_EVENT_30M] ?: false,
            notifEvent10m = preferences[PreferencesKeys.NOTIF_EVENT_10M] ?: false,
            notifEventAtTime = preferences[PreferencesKeys.NOTIF_EVENT_AT_TIME] ?: false,
            fyShowPastLaunches = preferences[PreferencesKeys.FY_SHOW_PAST_LAUNCHES] ?: true,
            fyShowPastEvents = preferences[PreferencesKeys.FY_SHOW_PAST_EVENTS] ?: true,
            devMode = preferences[PreferencesKeys.DEV_MODE] ?: false
        )
    }

    suspend fun updateEnableNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] = enabled
        }
    }

    suspend fun updateNotifLaunch24h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_24H] = enabled
        }
    }

    suspend fun updateNotifLaunch12h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_12H] = enabled
        }
    }

    suspend fun updateNotifLaunch1h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_1H] = enabled
        }
    }

    suspend fun updateNotifLaunch30m(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_30M] = enabled
        }
    }

    suspend fun updateNotifLaunch10m(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_10M] = enabled
        }
    }

    suspend fun updateNotifLaunchAtTime(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_LAUNCH_AT_TIME] = enabled
        }
    }

    suspend fun updateNotifEvent24h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_24H] = enabled
        }
    }

    suspend fun updateNotifEvent12h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_12H] = enabled
        }
    }

    suspend fun updateNotifEvent1h(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_1H] = enabled
        }
    }

    suspend fun updateNotifEvent30m(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_30M] = enabled
        }
    }

    suspend fun updateNotifEvent10m(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_10M] = enabled
        }
    }

    suspend fun updateNotifEventAtTime(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIF_EVENT_AT_TIME] = enabled
        }
    }

    suspend fun updateFyShowPastLaunches(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FY_SHOW_PAST_LAUNCHES] = enabled
        }
    }

    suspend fun updateFyShowPastEvents(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FY_SHOW_PAST_EVENTS] = enabled
        }
    }

    suspend fun updateDevMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEV_MODE] = enabled
        }
    }
}
