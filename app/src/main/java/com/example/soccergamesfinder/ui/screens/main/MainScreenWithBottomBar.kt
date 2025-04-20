package com.example.soccergamesfinder.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.soccergamesfinder.navigation.*
import com.example.soccergamesfinder.ui.components.BottomNavigationBar
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenWithBottomBar(navController: NavHostController, currentUserViewModel: CurrentUserViewModel) {
    // יצירת ה־ViewModels הגלובליים פעם אחת
    val fieldListViewModel: FieldListViewModel = hiltViewModel()
    val gameListViewModel: GameListViewModel = hiltViewModel()

    val bottomNavItems = listOf(
        BottomNavItem("בית", Routes.Home.route, Icons.Default.Home),
        BottomNavItem("פרופיל", Routes.UserProfile.route, Icons.Default.Person),
        BottomNavItem("משחקים", Routes.AllGames.route, Icons.Default.Place),
        BottomNavItem("מגרשים", Routes.AllFields.route, Icons.Default.List),
        BottomNavItem("התראות", Routes.Notifications.route, Icons.Default.Notifications)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route) {
                        popUpTo(Routes.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MainGraph.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            mainNavGraph(
                navController = navController,
                fieldListViewModel = fieldListViewModel,
                gameListViewModel = gameListViewModel,
                currentUserViewModel = currentUserViewModel
            )
        }
    }
}
