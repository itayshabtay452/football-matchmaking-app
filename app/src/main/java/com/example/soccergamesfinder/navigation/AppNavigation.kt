package com.example.soccergamesfinder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soccergamesfinder.ui.screens.main.MainScreenWithBottomBar
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentUserViewModel: CurrentUserViewModel = hiltViewModel()

    val isUserLoggedIn by currentUserViewModel.isUserLoggedIn.collectAsState()

    if (isUserLoggedIn) {
        MainScreenWithBottomBar(navController, currentUserViewModel)
    } else {
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
