package com.nominal.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nominal.data.models.Launch
import com.nominal.ui.theme.Background
import com.nominal.ui.theme.Foreground
import com.nominal.ui.theme.SubForeground
import com.nominal.utils.DateUtils

@Composable
fun HighlightLaunch(
    launch: Launch,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = "scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                onClick = {
                    pressed = true
                    onClick()
                },
                onClickLabel = "Open launch details"
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image with blur
            launch.image?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Launch image",
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(8.dp),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = 0.7f
                        }
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Background.copy(alpha = 0.3f),
                                    Background.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Foreground image (non-blurred)
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Launch image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
            }

            // Launch info overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = launch.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = Foreground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = launch.launchServiceProvider.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SubForeground
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateUtils.formatDate(launch.net),
                        style = MaterialTheme.typography.bodyMedium,
                        color = SubForeground
                    )
                }

                // Countdown timer
                TMinus(
                    launchDate = launch.net,
                    modifier = Modifier.padding(top = 8.dp),
                    large = false
                )
            }
        }
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(150)
            pressed = false
        }
    }
}

