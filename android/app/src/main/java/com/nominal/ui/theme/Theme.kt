package com.nominal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
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

@Composable
fun NominalTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
