package com.nominal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

data class ThemeSettings(
    val useDynamicColor: Boolean = true,
    val customSeedColor: Int? = null,
    val customAccentColor: Int? = null,
    val customBackgroundColor: Int? = null
)

class ThemeRepository(private val context: Context) {

    private object PreferencesKeys {
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val CUSTOM_SEED_COLOR = intPreferencesKey("custom_seed_color")
        val CUSTOM_ACCENT_COLOR = intPreferencesKey("custom_accent_color")
        val CUSTOM_BACKGROUND_COLOR = intPreferencesKey("custom_background_color")
    }

    val themeSettings: Flow<ThemeSettings> = context.themeDataStore.data.map { preferences ->
        ThemeSettings(
            useDynamicColor = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true,
            customSeedColor = preferences[PreferencesKeys.CUSTOM_SEED_COLOR],
            customAccentColor = preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR],
            customBackgroundColor = preferences[PreferencesKeys.CUSTOM_BACKGROUND_COLOR]
        )
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun setCustomSeedColor(color: Int) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_SEED_COLOR] = color
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = false
        }
    }

    suspend fun setCustomAccentColor(color: Int) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR] = color
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = false
        }
    }

    suspend fun setCustomBackgroundColor(color: Int) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_BACKGROUND_COLOR] = color
        }
    }

    suspend fun resetToDefaults() {
        context.themeDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
