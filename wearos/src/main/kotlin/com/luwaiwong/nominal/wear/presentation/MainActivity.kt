package com.luwaiwong.nominal.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.luwaiwong.nominal.wear.ui.theme.NominalWearTheme

/**
 * Main activity for WearOS app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NominalWearApp()
        }
    }
}

@Composable
fun NominalWearApp() {
    NominalWearTheme {
        val navController = rememberSwipeDismissableNavController()
        val viewModel: LaunchViewModel = viewModel()

        NominalNavHost(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun NominalNavHost(
    navController: NavHostController,
    viewModel: LaunchViewModel
) {
    val selectedLaunch by viewModel.selectedLaunch.collectAsState()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "launch_list"
    ) {
        composable("launch_list") {
            LaunchListScreen(
                viewModel = viewModel,
                onLaunchClick = { launch ->
                    viewModel.selectLaunch(launch)
                    navController.navigate("launch_detail")
                }
            )
        }

        composable("launch_detail") {
            selectedLaunch?.let { launch ->
                LaunchDetailScreen(
                    launch = launch,
                    onBack = {
                        viewModel.clearSelectedLaunch()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
