package com.example.soccergamesfinder.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.soccergamesfinder.ui.screens.DemoScreens
import com.example.soccergamesfinder.ui.screens.allfields.AllFieldsScreen
import com.example.soccergamesfinder.ui.screens.home.HomeScreen
import com.example.soccergamesfinder.ui.screens.home.HomeScreenNavActions
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
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
            val mainGraphBackStackEntry = remember { navController.getBackStackEntry(Routes.MainGraph.route) }
            val fieldListViewModel: FieldListViewModel = hiltViewModel(mainGraphBackStackEntry)
            val gameListViewModel: GameListViewModel = hiltViewModel(mainGraphBackStackEntry)

            HomeScreen(
                navActions = HomeScreenNavActions(
                    navigateToProfile = { navController.navigate(Routes.EditProfile.route) },
                    navigateToAllFields = { navController.navigate(Routes.AllFields.route) },
                    navigateToAllGames = { navController.navigate(Routes.AllGames.route) },
                    navigateToField = { fieldId -> navController.navigate("${Routes.Field.route}/$fieldId") },
                    navigateToGame = { gameId -> navController.navigate("${Routes.Game.route}/$gameId") },
                    navigateToLogin = {
                        navController.navigate(Routes.AuthGraph.route) {
                            popUpTo(Routes.Home.route) { inclusive = true }
                        }
                    }
                ),
                fieldListViewModel = fieldListViewModel,
                gameListViewModel = gameListViewModel
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
            val mainGraphBackStackEntry = remember { navController.getBackStackEntry(Routes.MainGraph.route) }
            val fieldListViewModel: FieldListViewModel = hiltViewModel(mainGraphBackStackEntry)
            val gameListViewModel: GameListViewModel = hiltViewModel(mainGraphBackStackEntry)


            AllFieldsScreen(
                fieldListViewModel = fieldListViewModel,
                gameListViewModel = gameListViewModel,
                onViewGamesClick = { fieldId ->
                    navController.navigate("${Routes.Field.route}/$fieldId")
                }
            )
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
                navigateToGame = { navController.navigate("${Routes.Game.route}/$it") }
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
