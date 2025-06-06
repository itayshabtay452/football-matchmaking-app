package com.example.soccergamesfinder.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.soccergamesfinder.ui.screens.login.LoginScreen
import com.example.soccergamesfinder.ui.screens.profile.CompleteProfileScreen

/**
 * Defines the authentication navigation graph.
 *
 * Includes:
 * - Login screen
 * - Complete profile screen
 *
 * Uses custom transition animations and handles navigation after login.
 *
 * @param navController The NavController used to navigate between screens
 * @param onLoginSuccess Callback triggered after successful login and profile setup
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
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
                onNavigateToHome = { onLoginSuccess() },
                onNavigateToCompleteProfile = {
                    navController.navigate(Routes.CompleteProfile.route)
                }
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
                onNavigateToHome = { onLoginSuccess() }
            )
        }
    }
}
