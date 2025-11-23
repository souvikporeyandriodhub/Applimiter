package com.souvik.timelock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.souvik.timelock.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        // ------------------ WELCOME ------------------
        composable("welcome") {
            WelcomeScreen(
                onStart = { navController.navigate("permissions") }
            )
        }

        // ------------------ PERMISSION SCREEN ------------------
        composable("permissions") {
            PermissionScreen(
                onPermissionsGranted = {
                    navController.navigate("choose_app")
                }
            )
        }

        // ------------------ CHOOSE APP SCREEN ------------------
        composable("choose_app") {
            ChooseAppScreen(
                onAppSelected = { pkg ->
                    // selection is saved INSIDE ChooseAppScreen ViewModel
                    navController.navigate("set_limit/$pkg")
                }
            )
        }

        // ------------------ SET LIMIT SCREEN ------------------
        composable(
            route = "set_limit/{pkg}",
            arguments = listOf(
                navArgument("pkg") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pkg = backStackEntry.arguments?.getString("pkg") ?: ""

            SetLimitScreen(
                packageName = pkg,
                onLimitSet = {
                    navController.navigate("screen_time") {
                        popUpTo("choose_app") { inclusive = true }
                    }
                }
            )
        }

        // ------------------ SCREEN TIME DASHBOARD ------------------
        composable("screen_time") {
            ScreenTimeScreen(
                navController = navController,
                onOpenSetLimit = { pkg ->
                    navController.navigate("set_limit/$pkg")
                }
            )
        }

        // ------------------ TIME UP SCREEN ------------------
        composable(
            route = "time_up/{pkg}/{name}",
            arguments = listOf(
                navArgument("pkg") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val appName = backStackEntry.arguments?.getString("name") ?: "App"

            TimeUpScreen(
                appName = appName,
                onOkClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
