package com.example.soccergamesfinder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soccergamesfinder.ui.auth.LoginScreen
import com.example.soccergamesfinder.ui.auth.ProfileCompletionScreen
import com.example.soccergamesfinder.ui.auth.RegisterScreen
import com.example.soccergamesfinder.ui.HomeScreen
import com.example.soccergamesfinder.ui.ProfileDetailsScreen
import com.example.soccergamesfinder.ui.FieldsListScreen

object Routes {
    const val Login = "login"
    const val Register = "register"
    const val ProfileCompletion = "profileCompletion"
    const val Home = "home"
    const val ProfileDetails = "profile_details_screen"
    const val FieldsList = "fields_list_screen"
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

        composable(Routes.ProfileDetails) {
            ProfileDetailsScreen(navController = navController)
        }

        composable(Routes.FieldsList) {
            FieldsListScreen(navController = navController)
        }
    }
}
