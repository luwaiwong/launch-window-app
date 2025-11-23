package com.nominal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nominal.data.models.Launch
import com.nominal.ui.components.TMinus
import com.nominal.ui.theme.*
import com.nominal.ui.viewmodels.DataState
import com.nominal.ui.viewmodels.MainViewModel
import com.nominal.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailScreen(
    launchId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val dataState by viewModel.dataState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Launch Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = Foreground,
                    navigationIconContentColor = Foreground
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        when (val state = dataState) {
            is DataState.Success -> {
                val launch = state.data.launches?.upcoming?.find { it.id == launchId }
                    ?: state.data.launches?.previous?.find { it.id == launchId }

                if (launch != null) {
                    LaunchDetailContent(
                        launch = launch,
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Launch not found", color = Foreground)
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun LaunchDetailContent(
    launch: Launch,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Launch image
        launch.image?.let { imageUrl ->
            item {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Launch image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Title and status
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = launch.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Foreground
                )

                val statusColor = when {
                    launch.isSuccessful() -> StatusSuccess
                    launch.isFailed() -> StatusError
                    launch.isPartialFailure() -> StatusWarning
                    else -> Accent
                }

                Text(
                    text = launch.status.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor
                )
            }
        }

        // Countdown
        if (!launch.isPast()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BackgroundHighlight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Time Until Launch",
                            style = MaterialTheme.typography.titleMedium,
                            color = SubForeground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TMinus(launchDate = launch.net, large = true)
                    }
                }
            }
        }

        // Date & Time
        item {
            DetailSection(
                title = "Date & Time",
                content = DateUtils.formatDate(launch.net)
            )
        }

        // Rocket
        item {
            DetailSection(
                title = "Rocket",
                content = launch.rocket.configuration.fullName ?: launch.rocket.configuration.name
            )
        }

        // Provider
        item {
            DetailSection(
                title = "Launch Service Provider",
                content = launch.launchServiceProvider.name
            )
        }

        // Pad
        item {
            DetailSection(
                title = "Launch Pad",
                content = "${launch.pad.name}, ${launch.pad.location?.name ?: "Unknown"}"
            )
        }

        // Mission description
        launch.mission?.description?.let { description ->
            item {
                DetailSection(
                    title = "Mission",
                    content = description
                )
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BackgroundHighlight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = SubForeground
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = Foreground
            )
        }
    }
}
