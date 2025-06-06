package com.example.soccergamesfinder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.soccergamesfinder.ui.screens.main.MainScreenWithBottomBar
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

/**
 * The main navigation entry point of the app.
 *
 * Displays either the authentication graph or the main app content,
 * depending on whether the user is logged in.
 *
 * Uses Hilt to provide the CurrentUserViewModel and observes login state
 * via a StateFlow.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentUserViewModel: CurrentUserViewModel = hiltViewModel()

    // Observe login state from ViewModel
    val isUserLoggedIn by currentUserViewModel.isUserLoggedIn.collectAsState()

    if (isUserLoggedIn) {
        // User is logged in – show main app content with bottom bar
        MainScreenWithBottomBar(navController, currentUserViewModel)
    } else {
        // User not logged in – show auth flow
        NavHost(
            navController = navController,
            startDestination = Routes.AuthGraph.route
        ) {
            authNavGraph(
                navController = navController,
                onLoginSuccess = {
                    currentUserViewModel.setLoggedIn(true)
                }
            )
        }
    }
}
