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
import com.example.soccergamesfinder.viewmodel.UserViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.example.soccergamesfinder.viewmodel.LocationViewModel


@Composable
fun AppNavigation(authViewModel: AuthViewModel, userViewModel: UserViewModel,
                  locationViewModel: LocationViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login",
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()){
            LoginScreen(authViewModel, navigateToHome = {navController.navigate("home")},
                navigateToCompleteProfile= {navController.navigate("complete_profile")})
        }

        composable("home",
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
            ) {
            HomeScreen(authViewModel,userViewModel,
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
            CompleteProfileScreen( navigateToHome = {navController.navigate("home")},
                locationViewModel)
        }

        composable(
            "fieldScreen/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            FieldScreen(
                fieldId = fieldId,
                navigateToCreateGame = { fieldId -> navController.navigate("createGame/$fieldId")
                },
                navigateToGame = {gameId: String ->
                    navController.navigate("gameScreen/$gameId")
                }
            )
        }

        composable(
            "createGame/{fieldId}",
            arguments = listOf(navArgument("fieldId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
            CreateGameScreen(fieldId = fieldId, userViewModel = userViewModel,
                navigateBack = {navController.popBackStack()})
        }

        composable(
            "gameScreen/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GameScreen(gameId, userViewModel = userViewModel, navigateBack = {navController.popBackStack()})
        }

    }
}



@OptIn(ExperimentalAnimationApi::class)
fun defaultEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
    { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
    { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultPopEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
    { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultPopExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
    { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
