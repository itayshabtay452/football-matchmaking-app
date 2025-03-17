package com.example.soccergamesfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soccergamesfinder.ui.screens.CompleteProfileScreen
import com.example.soccergamesfinder.ui.screens.CreateGameScreen
import com.example.soccergamesfinder.ui.screens.HomeScreen
import com.example.soccergamesfinder.ui.screens.LoginScreen
import com.example.soccergamesfinder.ui.screens.FieldScreen
import com.example.soccergamesfinder.ui.screens.GameScreen
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel


@Composable
fun AppNavigation(authViewModel: AuthViewModel, userViewModel: UserViewModel, fieldViewModel: FieldViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login"){
            LoginScreen(authViewModel,userViewModel, navigateToHome = {navController.navigate("home")},
                navigateToCompleteProfile= {navController.navigate("complete_profile")})
        }

        composable("home") {
            HomeScreen(authViewModel,userViewModel,fieldViewModel,
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                navigateToField = {fieldId -> navController.navigate("fieldScreen/$fieldId") {
                    popUpTo("home") {inclusive = false}
                    launchSingleTop = true }
                }
                )}

        composable("complete_profile"){
            CompleteProfileScreen(userViewModel,
                navigateToHome = {navController.navigate("home")})
        }

        composable(
            "fieldScreen/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType })
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            FieldScreen(
                fieldId = fieldId, fieldViewModel = fieldViewModel,
                navigateToCreateGame = { fieldId -> navController.navigate("createGame/$fieldId")
                },
                navigateToGame = {gameId: String ->
                    navController.navigate("gameScreen/$gameId")
                }
            )
        }

        composable(
            "createGame/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType })
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            CreateGameScreen(fieldId = fieldId, userViewModel = userViewModel,
                navigateBack = {navController.popBackStack()})
        }

        composable(
            "gameScreen/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GameScreen(gameId)
        }


    }
}
