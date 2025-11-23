package com.nominal.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.nominal.ui.screens.DashboardScreen
import com.nominal.ui.screens.ForYouScreen
import com.nominal.ui.screens.LaunchesScreen
import com.nominal.ui.screens.SettingsScreen
import com.nominal.ui.theme.Background
import com.nominal.ui.theme.Foreground
import com.nominal.ui.viewmodels.MainViewModel

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object ForYou : BottomNavItem("for_you", "For You", Icons.Default.Star)
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Home)
    object Launches : BottomNavItem("launches", "Launches", Icons.Default.DateRange)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLaunchClick: (String) -> Unit,
    onEventClick: (Int) -> Unit,
    onArticleClick: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(1) } // Start with Dashboard

    val items = listOf(
        BottomNavItem.ForYou,
        BottomNavItem.Dashboard,
        BottomNavItem.Launches,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Background,
                contentColor = Foreground
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Foreground,
                            selectedTextColor = Foreground,
                            indicatorColor = Background,
                            unselectedIconColor = Foreground.copy(alpha = 0.6f),
                            unselectedTextColor = Foreground.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        },
        containerColor = Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> ForYouScreen(
                    viewModel = viewModel,
                    onLaunchClick = onLaunchClick,
                    onEventClick = onEventClick
                )
                1 -> DashboardScreen(
                    viewModel = viewModel,
                    onLaunchClick = onLaunchClick,
                    onEventClick = onEventClick,
                    onArticleClick = onArticleClick
                )
                2 -> LaunchesScreen(
                    viewModel = viewModel,
                    onLaunchClick = onLaunchClick
                )
                3 -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
