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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nominal.data.models.Launch
import com.nominal.ui.theme.*
import com.nominal.utils.DateUtils

@Composable
fun LaunchCard(
    launch: Launch,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
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
        colors = CardDefaults.cardColors(
            containerColor = BackgroundHighlight
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Launch image
            launch.image?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Launch image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Launch info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = launch.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Foreground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = launch.launchServiceProvider.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = SubForeground
                )

                Text(
                    text = DateUtils.formatRelativeTime(launch.net),
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        launch.isSuccessful() -> StatusSuccess
                        launch.isFailed() -> StatusError
                        launch.isPartialFailure() -> StatusWarning
                        else -> Accent
                    }
                )
            }

            // Status badge
            StatusBadge(launch)
        }
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(150)
            pressed = false
        }
    }
}

@Composable
private fun StatusBadge(launch: Launch) {
    val statusPair: Pair<String, Color> = when {
        launch.isSuccessful() -> Pair("Success", StatusSuccess)
        launch.isFailed() -> Pair("Failed", StatusError)
        launch.isPartialFailure() -> Pair("Partial", StatusWarning)
        else -> Pair(launch.status.abbrev ?: "TBD", Accent)
    }
    val statusText = statusPair.first
    val statusColor = statusPair.second

    Box(
        modifier = Modifier
            .background(
                color = statusColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall,
            color = statusColor
        )
    }
}
