package com.luwaiwong.nominal.wear.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.luwaiwong.nominal.wear.data.Launch
import com.luwaiwong.nominal.wear.ui.theme.NominalColors

/**
 * Launch detail screen showing full information about a launch
 */
@Composable
fun LaunchDetailScreen(
    launch: Launch,
    onBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = listState
            )
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(
                top = 32.dp,
                start = 12.dp,
                end = 12.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Launch name
            item {
                Text(
                    text = launch.name,
                    style = MaterialTheme.typography.title3,
                    color = NominalColors.OnBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Countdown
            item {
                Card(
                    onClick = {},
                    backgroundPainter = CardDefaults.cardBackgroundPainter(
                        startBackgroundColor = NominalColors.Primary.copy(alpha = 0.3f),
                        endBackgroundColor = NominalColors.Surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Launch in",
                            style = MaterialTheme.typography.caption2,
                            color = NominalColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = launch.getCountdown(),
                            style = MaterialTheme.typography.display1,
                            color = NominalColors.Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Provider
            item {
                DetailItem(
                    label = "Provider",
                    value = launch.provider.name
                )
            }

            // Rocket
            item {
                DetailItem(
                    label = "Rocket",
                    value = launch.rocket.name
                )
            }

            // Launch pad
            launch.pad?.let { pad ->
                item {
                    DetailItem(
                        label = "Location",
                        value = pad.location
                    )
                }
            }

            // Mission
            launch.mission?.let { mission ->
                item {
                    DetailItem(
                        label = "Mission",
                        value = mission.name
                    )
                }

                mission.description?.let { description ->
                    item {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption2,
                            color = NominalColors.TextSecondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Launch time
            item {
                DetailItem(
                    label = "Launch Time",
                    value = launch.getFormattedDateTime()
                )
            }

            // Status
            item {
                DetailItem(
                    label = "Status",
                    value = launch.status.name
                )
            }

            // Back button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Chip(
                    onClick = onBack,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = NominalColors.Surface
                    ),
                    label = { Text("Back") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption3,
            color = NominalColors.TextTertiary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.caption1,
            color = NominalColors.OnBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
