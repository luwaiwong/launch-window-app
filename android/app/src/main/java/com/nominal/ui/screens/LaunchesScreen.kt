package com.nominal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nominal.data.models.CachedData
import com.nominal.ui.components.LaunchCard
import com.nominal.ui.theme.Accent
import com.nominal.ui.theme.Foreground
import com.nominal.ui.theme.SubForeground
import com.nominal.ui.viewmodels.DataState
import com.nominal.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LaunchesScreen(
    viewModel: MainViewModel,
    onLaunchClick: (String) -> Unit
) {
    val dataState by viewModel.dataState.collectAsState()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Row
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            contentColor = Foreground
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        text = "Upcoming",
                        color = if (pagerState.currentPage == 0) Accent else SubForeground
                    )
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        text = "Previous",
                        color = if (pagerState.currentPage == 1) Accent else SubForeground
                    )
                }
            )
        }

        // Content
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (val state = dataState) {
                is DataState.Success -> {
                    val launches = if (page == 0) {
                        state.data.launches?.upcoming ?: emptyList()
                    } else {
                        state.data.launches?.previous ?: emptyList()
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(launches) { launch ->
                            LaunchCard(
                                launch = launch,
                                onClick = { onLaunchClick(launch.id) }
                            )
                        }
                    }
                }
                is DataState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Foreground,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                        )
                        androidx.compose.material3.Button(
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
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
