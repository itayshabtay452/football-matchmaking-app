package com.example.soccergamesfinder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soccergamesfinder.ui.auth.LoginScreen
import com.example.soccergamesfinder.ui.auth.ProfileCompletionScreen
import com.example.soccergamesfinder.ui.auth.RegisterScreen
import com.example.soccergamesfinder.ui.HomeScreen

object Routes {
    const val Login = "login"
    const val Register = "register"
    const val ProfileCompletion = "profileCompletion"
    const val Home = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login) {
        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    // נניח שכאן נבדוק אם הפרופיל הושלם, אבל לעת עתה ננווט ישירות למסך הבית:
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
                    // לאחר ההרשמה (אימייל וסיסמה בלבד), נוודא שהמשתמש ימלא את פרופילו
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
    }
}
