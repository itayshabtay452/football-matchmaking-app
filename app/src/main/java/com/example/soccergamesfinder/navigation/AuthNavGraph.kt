package com.example.soccergamesfinder.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.soccergamesfinder.ui.screens.login.LoginScreen
import com.example.soccergamesfinder.ui.screens.profile.CompleteProfileScreen

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
            LoginScreen(
                onNavigateToHome = { navController.navigate(Routes.MainGraph.route) },
                onNavigateToCompleteProfile = { navController.navigate(Routes.CompleteProfile.route) }
            )
        }
        composable(
            route = Routes.CompleteProfile.route,
            enterTransition = defaultEnterTransition(),
            exitTransition = defaultExitTransition(),
            popEnterTransition = defaultPopEnterTransition(),
            popExitTransition = defaultPopExitTransition()
        ) {
            CompleteProfileScreen(
                onNavigateToHome = { navController.navigate(Routes.MainGraph.route) }
            )
        }
    }
}
