package com.nominal.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nominal.ui.screens.LaunchDetailScreen
import com.nominal.ui.viewmodels.MainViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object LaunchDetail : Screen("launch/{launchId}") {
        fun createRoute(launchId: String) = "launch/$launchId"
    }
}

@Composable
fun NominalNavigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel,
                onLaunchClick = { launchId ->
                    navController.navigate(Screen.LaunchDetail.createRoute(launchId))
                },
                onEventClick = { eventId ->
                    // Navigate to event detail (simplified - can be expanded)
                },
                onArticleClick = { url ->
                    // Open URL in browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }

        composable(
            route = Screen.LaunchDetail.route,
            arguments = listOf(
                navArgument("launchId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val launchId = backStackEntry.arguments?.getString("launchId") ?: ""
            LaunchDetailScreen(
                launchId = launchId,
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
