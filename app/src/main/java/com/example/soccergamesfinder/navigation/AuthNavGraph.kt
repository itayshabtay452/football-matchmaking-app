package com.example.soccergamesfinder.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.soccergamesfinder.ui.screens.DemoScreens

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Routes.Login.route,
        route = Routes.AuthGraph.route
    ) {
        composable(
            route = Routes.Login.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.LoginScreen(
                navigateToHome = { navController.navigate(Routes.MainGraph.route) },
                navigateToCompleteProfile = { navController.navigate(Routes.CompleteProfile.route) }
            )
        }
        composable(
            route = Routes.CompleteProfile.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            DemoScreens.CompleteProfileScreen(
                navigateToHome = { navController.navigate(Routes.MainGraph.route) }
            )
        }
    }
}
