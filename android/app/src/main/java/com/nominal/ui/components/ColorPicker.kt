package com.nominal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun ColorPicker(
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedColor by remember { mutableStateOf(Color(0xFF5E81AC)) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Color Wheel
        ColorWheel(
            onColorSelected = { color ->
                selectedColor = color
                onColorSelected(color)
            },
            modifier = Modifier.size(250.dp)
        )

        // Selected Color Preview
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(selectedColor, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )

        // Color value display
        Text(
            text = colorToHex(selectedColor),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ColorWheel(
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPosition by remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = min(size.width, size.height) / 2f
                    val distance = sqrt(
                        (offset.x - center.x).pow(2) + (offset.y - center.y).pow(2)
                    )

                    if (distance <= radius) {
                        selectedPosition = offset
                        val color = getColorAtPosition(offset, center, radius)
                        onColorSelected(color)
                    }
                }
            }
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = min(size.width, size.height) / 2f

        // Draw color wheel
        for (angle in 0..360 step 1) {
            val rad = Math.toRadians(angle.toDouble()).toFloat()
            val color = Color.hsv(angle.toFloat(), 1f, 1f)

            drawLine(
                color = color,
                start = center,
                end = Offset(
                    center.x + radius * cos(rad),
                    center.y + radius * sin(rad)
                ),
                strokeWidth = 3f
            )
        }

        // Draw brightness gradient (center to edge)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.Transparent),
                center = center,
                radius = radius * 0.8f
            ),
            radius = radius,
            center = center
        )

        // Draw selection indicator
        selectedPosition?.let { pos ->
            drawCircle(
                color = Color.White,
                radius = 10f,
                center = pos,
                style = Stroke(width = 3f)
            )
            drawCircle(
                color = Color.Black,
                radius = 10f,
                center = pos,
                style = Stroke(width = 1f)
            )
        }
    }
}

private fun getColorAtPosition(position: Offset, center: Offset, radius: Float): Color {
    val dx = position.x - center.x
    val dy = position.y - center.y
    val angle = (atan2(dy, dx) * 180 / PI + 360) % 360
    val distance = sqrt(dx.pow(2) + dy.pow(2))
    val saturation = (distance / radius).coerceIn(0f, 1f)

    return Color.hsv(angle.toFloat(), saturation, 1f)
}

private fun colorToHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}
