package com.example.soccergamesfinder.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.soccergamesfinder.ui.screens.DemoScreens

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(
        startDestination = Routes.Home.route,
        route = Routes.MainGraph.route
    ) {
        // Home Screen (Dashboard)
        composable(
            route = Routes.Home.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.HomeScreen(
                navigateToLogin = {
                    navController.navigate(Routes.AuthGraph.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                },
                navigateToField = { fieldId ->
                    navController.navigate("${Routes.Field.route}/$fieldId")
                },
                navigateToAddField = { navController.navigate(Routes.AddField.route) }
            )
        }
        // All Fields Screen
        composable(
            route = Routes.AllFields.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.AllFieldsScreen { fieldId ->
                navController.navigate("${Routes.Field.route}/$fieldId")
            }
        }
        // All Games Screen
        composable(
            route = Routes.AllGames.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.AllGamesScreen { gameId ->
                navController.navigate("${Routes.Game.route}/$gameId")
            }
        }
        // Edit Profile Screen
        composable(
            route = Routes.EditProfile.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.EditProfileScreen(navigateBack = { navController.popBackStack() })
        }
        // Notifications Screen
        composable(
            route = Routes.Notifications.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.NotificationsScreen(navigateBack = { navController.popBackStack() })
        }
        // User History Screen
        composable(
            route = Routes.UserHistory.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.UserHistoryScreen(navigateBack = { navController.popBackStack() })
        }
        // Add Field Screen
        composable(
            route = Routes.AddField.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.AddFieldScreen(navigateBack = { navController.popBackStack() })
        }
        // Field Screen
        composable(
            route = "${Routes.Field.route}/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            DemoScreens.FieldScreen(
                fieldId = fieldId,
                navigateToCreateGame = { navController.navigate("${Routes.CreateGame.route}/$it") },
                navigateToGame = { navController.navigate("${Routes.Game.route}/$it") }
            )
        }
        // Create Game Screen
        composable(
            route = "${Routes.CreateGame.route}/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            DemoScreens.CreateGameScreen(
                fieldId = fieldId,
                navigateBack = { navController.popBackStack() }
            )
        }
        // Game Screen
        composable(
            route = "${Routes.Game.route}/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            DemoScreens.GameScreen(
                gameId = gameId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
