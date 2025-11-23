package com.nominal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nominal.data.models.CachedData
import com.nominal.ui.components.*
import com.nominal.ui.theme.Foreground
import com.nominal.ui.theme.SubForeground
import com.nominal.ui.viewmodels.DataState
import com.nominal.ui.viewmodels.MainViewModel

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onLaunchClick: (String) -> Unit,
    onEventClick: (Int) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val dataState by viewModel.dataState.collectAsState()
    val isRefreshing = dataState is DataState.Loading

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.loadData(forceRefresh = true) }
    ) {
        when (val state = dataState) {
            is DataState.Success -> {
                DashboardContent(
                    data = state.data,
                    viewModel = viewModel,
                    onLaunchClick = onLaunchClick,
                    onEventClick = onEventClick,
                    onArticleClick = onArticleClick
                )
            }
            is DataState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        color = Foreground
                    )
                }
            }
            is DataState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    data: CachedData,
    viewModel: MainViewModel,
    onLaunchClick: (String) -> Unit,
    onEventClick: (Int) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val repository = viewModel.getRepository()
    val highlightLaunch = repository.getDashboardHighlightLaunch(data.launches)
    val recentLaunches = repository.getDashboardRecentLaunches(data.launches)
    val upcomingLaunches = repository.getDashboardFilteredLaunches(data.launches)
    val events = repository.getDashboardEvents(data.events)
    val articles = repository.getDashboardArticles(data.articles)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        // Highlight Launch
        highlightLaunch?.let { launch ->
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Next Highlight",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Foreground
                    )
                    HighlightLaunch(
                        launch = launch,
                        onClick = { onLaunchClick(launch.id) }
                    )
                }
            }
        }

        // Upcoming Launches
        if (upcomingLaunches.isNotEmpty()) {
            item {
                Text(
                    text = "Upcoming Launches",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Foreground
                )
            }
            items(upcomingLaunches) { launch ->
                LaunchCard(
                    launch = launch,
                    onClick = { onLaunchClick(launch.id) }
                )
            }
        }

        // Events
        if (events.isNotEmpty()) {
            item {
                Text(
                    text = "Upcoming Events",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Foreground
                )
            }
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            }
        }

        // Articles
        if (articles.isNotEmpty()) {
            item {
                Text(
                    text = "Latest News",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Foreground
                )
            }
            items(articles) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onArticleClick(article.url) }
                )
            }
        }
    }
}
