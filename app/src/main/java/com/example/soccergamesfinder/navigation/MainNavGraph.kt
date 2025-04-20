package com.example.soccergamesfinder.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.soccergamesfinder.ui.screens.allfields.AllFieldsScreen
import com.example.soccergamesfinder.ui.screens.allgames.AllGamesScreen
import com.example.soccergamesfinder.ui.screens.field.FieldDetailsScreen
import com.example.soccergamesfinder.ui.screens.game.GameDetailsScreen
import com.example.soccergamesfinder.ui.screens.home.HomeScreen
import com.example.soccergamesfinder.ui.screens.home.HomeScreenNavActions
import com.example.soccergamesfinder.ui.screens.user.UserViewScreen
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
            val currentUserViewModel: CurrentUserViewModel = hiltViewModel(mainGraphBackStackEntry)

            HomeScreen(
                navActions = HomeScreenNavActions(
                    navigateToProfile = { userId -> navController.navigate("${Routes.UserProfile.route}/$userId")},
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
                gameListViewModel = gameListViewModel,
                currentUserViewModel = currentUserViewModel
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

            AllFieldsScreen(
                fieldListViewModel = fieldListViewModel,
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
            val mainGraphBackStackEntry = remember { navController.getBackStackEntry(Routes.MainGraph.route) }
            val fieldListViewModel: FieldListViewModel = hiltViewModel(mainGraphBackStackEntry)
            val gameListViewModel: GameListViewModel = hiltViewModel(mainGraphBackStackEntry)
            val currentUserViewModel: CurrentUserViewModel = hiltViewModel(mainGraphBackStackEntry)

            AllGamesScreen(
                onGameClick = { gameId ->
                    navController.navigate("${Routes.Game.route}/$gameId")
                },
                gameListViewModel = gameListViewModel,
                fieldListViewModel = fieldListViewModel,
                currentUserViewModel = currentUserViewModel
            )

        }

        // Field Details Screen
        composable(
            route = "${Routes.Field.route}/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->

            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: return@composable

            val mainGraphBackStackEntry = remember { navController.getBackStackEntry(Routes.MainGraph.route) }
            val currentUserViewModel: CurrentUserViewModel = hiltViewModel(mainGraphBackStackEntry)

            FieldDetailsScreen(
                fieldId = fieldId,
                currentUserViewModel = currentUserViewModel,
                onNavigateToGame = { gameId ->
                    navController.navigate("${Routes.Game.route}/$gameId")
                }
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
        ) {
            val gameId = it.arguments?.getString("gameId") ?: return@composable

            GameDetailsScreen(
                gameId = gameId,
                onNavigateToField = { fieldId ->
                    navController.navigate("${Routes.Field.route}/$fieldId")
                },
                onNavigateToUser = { userId ->
                    navController.navigate("${Routes.UserProfile.route}/$userId")

                }
            )
        }

        // User Profile Screen
        composable(
            route = "${Routes.UserProfile.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable

            UserViewScreen(
                userId = userId,
                onNavigateToGame = { gameId ->
                    navController.navigate("${Routes.Game.route}/$gameId")
                }
            )
        }

    }
}
