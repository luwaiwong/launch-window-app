package com.nominal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nominal.data.models.Event
import com.nominal.data.models.Launch
import com.nominal.ui.components.EventCard
import com.nominal.ui.components.HighlightLaunch
import com.nominal.ui.theme.Foreground
import com.nominal.ui.viewmodels.DataState
import com.nominal.ui.viewmodels.MainViewModel

@Composable
fun ForYouScreen(
    viewModel: MainViewModel,
    onLaunchClick: (String) -> Unit,
    onEventClick: (Int) -> Unit
) {
    val dataState by viewModel.dataState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    when (val state = dataState) {
        is DataState.Success -> {
            val repository = viewModel.getRepository()
            val forYouItems = repository.getForYouData(
                launches = state.data.launches,
                events = state.data.events,
                showPastLaunches = settings.fyShowPastLaunches,
                showPastEvents = settings.fyShowPastEvents
            )

            if (forYouItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You're all caught up!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Foreground
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    items(forYouItems) { item ->
                        when (item) {
                            is Launch -> {
                                HighlightLaunch(
                                    launch = item,
                                    onClick = { onLaunchClick(item.id) }
                                )
                            }
                            is Event -> {
                                EventCard(
                                    event = item,
                                    onClick = { onEventClick(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
        is DataState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Foreground,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                )
                Button(
                    onClick = { viewModel.loadData(forceRefresh = true) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Retry")
                }
            }
        }
        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }
    }
}
