package com.nominal.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nominal.data.repository.ThemeSettings

@Composable
fun NominalTheme(
    themeSettings: ThemeSettings = ThemeSettings(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()

    val colorScheme = when {
        // Use dynamic colors on Android 12+ if enabled
        themeSettings.useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicDarkColorScheme(context)
        }
        // Use custom colors if provided
        themeSettings.customSeedColor != null -> {
            createCustomColorScheme(
                seedColor = Color(themeSettings.customSeedColor),
                accentColor = themeSettings.customAccentColor?.let { Color(it) },
                backgroundColor = themeSettings.customBackgroundColor?.let { Color(it) }
            )
        }
        // Fall back to default dark theme
        else -> {
            defaultDarkColorScheme()
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun createCustomColorScheme(
    seedColor: Color,
    accentColor: Color? = null,
    backgroundColor: Color? = null
): androidx.compose.material3.ColorScheme {
    val primaryColor = accentColor ?: seedColor
    val bgColor = backgroundColor ?: Background

    return darkColorScheme(
        primary = primaryColor,
        onPrimary = Foreground,
        secondary = primaryColor,
        onSecondary = Foreground,
        tertiary = primaryColor,
        onTertiary = Foreground,
        background = bgColor,
        onBackground = Foreground,
        surface = BackgroundHighlight,
        onSurface = Foreground,
        surfaceVariant = Surface,
        onSurfaceVariant = SubForeground,
        error = StatusError,
        onError = Foreground,
        outline = SubForeground,
        outlineVariant = Color(0xFF3A3C3D),
        primaryContainer = primaryColor.copy(alpha = 0.3f),
        onPrimaryContainer = Foreground
    )
}

private fun defaultDarkColorScheme(): androidx.compose.material3.ColorScheme {
    return darkColorScheme(
        primary = Accent,
        onPrimary = Foreground,
        secondary = Accent,
        onSecondary = Foreground,
        tertiary = Accent,
        onTertiary = Foreground,
        background = Background,
        onBackground = Foreground,
        surface = BackgroundHighlight,
        onSurface = Foreground,
        surfaceVariant = Surface,
        onSurfaceVariant = SubForeground,
        error = StatusError,
        onError = Foreground,
        outline = SubForeground,
        outlineVariant = Color(0xFF3A3C3D)
    )
}
