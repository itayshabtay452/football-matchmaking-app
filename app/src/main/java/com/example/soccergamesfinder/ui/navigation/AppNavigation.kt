package com.example.soccergamesfinder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soccergamesfinder.ui.auth.LoginScreen
import com.example.soccergamesfinder.ui.auth.ProfileCompletionScreen
import com.example.soccergamesfinder.ui.auth.RegisterScreen
import com.example.soccergamesfinder.ui.HomeScreen
import com.example.soccergamesfinder.ui.FieldsScreen

object Routes {
    const val Login = "login"
    const val Register = "register"
    const val ProfileCompletion = "profileCompletion"
    const val Home = "home"
    const val Fields = "fields_screens"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login) {
        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register)
                }
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.ProfileCompletion) {
                        popUpTo(Routes.Login) { inclusive = false }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.ProfileCompletion) {
            ProfileCompletionScreen(
                onProfileCompleteSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.ProfileCompletion) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Home) {
            HomeScreen(navController = navController)
        }

        composable(
            route = "fields_screen/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble() ?: 0.0
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble() ?: 0.0

            FieldsScreen(navController, latitude, longitude)
        }


    }
}
