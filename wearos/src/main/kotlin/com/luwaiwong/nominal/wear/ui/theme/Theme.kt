package com.luwaiwong.nominal.wear.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme

/**
 * Theme colors for WearOS app
 */
object NominalColors {
    val Primary = Color(0xFF4CAF50) // Green for countdown
    val PrimaryVariant = Color(0xFF388E3C)
    val Secondary = Color(0xFF03DAC6)
    val Background = Color(0xFF1E1E1E)
    val Surface = Color(0xFF2C2C2C)
    val Error = Color(0xFFCF6679)
    val OnPrimary = Color.White
    val OnSecondary = Color.Black
    val OnBackground = Color.White
    val OnSurface = Color.White
    val OnError = Color.Black
    val TextSecondary = Color(0xFFAAAAAA)
    val TextTertiary = Color(0xFF888888)
}

@Composable
fun NominalWearTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = androidx.wear.compose.material.Colors(
            primary = NominalColors.Primary,
            primaryVariant = NominalColors.PrimaryVariant,
            secondary = NominalColors.Secondary,
            secondaryVariant = NominalColors.Secondary,
            background = NominalColors.Background,
            surface = NominalColors.Surface,
            error = NominalColors.Error,
            onPrimary = NominalColors.OnPrimary,
            onSecondary = NominalColors.OnSecondary,
            onBackground = NominalColors.OnBackground,
            onSurface = NominalColors.OnSurface,
            onSurfaceVariant = NominalColors.TextSecondary,
            onError = NominalColors.OnError
        ),
        content = content
    )
}
