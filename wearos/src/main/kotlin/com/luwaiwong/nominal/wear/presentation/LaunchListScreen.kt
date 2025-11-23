package com.luwaiwong.nominal.wear.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
 * Launch list screen for WearOS
 */
@Composable
fun LaunchListScreen(
    viewModel: LaunchViewModel,
    onLaunchClick: (Launch) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = rememberScalingLazyListState()) }
    ) {
        when (val state = uiState) {
            is LaunchUiState.Loading -> {
                LoadingScreen()
            }
            is LaunchUiState.Success -> {
                LaunchList(
                    launches = state.launches,
                    onLaunchClick = onLaunchClick
                )
            }
            is LaunchUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.refresh() }
                )
            }
            is LaunchUiState.Empty -> {
                EmptyScreen()
            }
        }
    }
}

@Composable
fun LaunchList(
    launches: List<Launch>,
    onLaunchClick: (Launch) -> Unit
) {
    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            top = 32.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            ListHeader(title = "Upcoming Launches")
        }

        items(launches) { launch ->
            LaunchItem(
                launch = launch,
                onClick = { onLaunchClick(launch) }
            )
        }
    }
}

@Composable
fun LaunchItem(
    launch: Launch,
    onClick: () -> Unit
) {
    Chip(
        onClick = onClick,
        colors = ChipDefaults.chipColors(
            backgroundColor = NominalColors.Surface,
            contentColor = NominalColors.OnSurface
        ),
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = launch.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption1
            )
        },
        secondaryLabel = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = launch.getCountdown(),
                    color = NominalColors.Primary,
                    style = MaterialTheme.typography.caption2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = launch.provider.name,
                    color = NominalColors.TextSecondary,
                    style = MaterialTheme.typography.caption3,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }
        }
    )
}

@Composable
fun ListHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.title3,
            color = NominalColors.Primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            indicatorColor = NominalColors.Primary,
            trackColor = NominalColors.Surface
        )
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.title3,
            color = NominalColors.Error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.caption2,
            color = NominalColors.TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Chip(
            onClick = onRetry,
            colors = ChipDefaults.chipColors(
                backgroundColor = NominalColors.Primary
            ),
            label = { Text("Retry") }
        )
    }
}

@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Launches",
            style = MaterialTheme.typography.title3,
            color = NominalColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Check back later",
            style = MaterialTheme.typography.caption2,
            color = NominalColors.TextTertiary,
            textAlign = TextAlign.Center
        )
    }
}
