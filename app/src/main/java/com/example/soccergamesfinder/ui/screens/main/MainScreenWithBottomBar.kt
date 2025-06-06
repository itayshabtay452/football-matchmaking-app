package com.example.soccergamesfinder.ui.screens.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.soccergamesfinder.navigation.*
import com.example.soccergamesfinder.navigation.BottomNavigationBar
import com.example.soccergamesfinder.ui.screens.notifications.NotificationsViewModel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenWithBottomBar(navController: NavHostController, currentUserViewModel: CurrentUserViewModel) {
    // יצירת ה־ViewModels הגלובליים פעם אחת
    val fieldListViewModel: FieldListViewModel = hiltViewModel()
    val gameListViewModel: GameListViewModel = hiltViewModel()
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()

    val user = currentUserViewModel.state.collectAsState().value.user

    // מספר התראות שלא נקראו
    val unreadCount by notificationsViewModel.unreadCount.collectAsState()

    LaunchedEffect(user?.id) {
        user?.id?.let { userId ->
            notificationsViewModel.startListening(userId)
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem("בית", Routes.Home.route, Icons.Default.Home),
        BottomNavItem("משחקים", Routes.AllGames.route, Icons.Default.Place),
        BottomNavItem("מגרשים", Routes.AllFields.route, Icons.AutoMirrored.Filled.List),
        BottomNavItem("התראות", Routes.Notifications.route, Icons.Default.Notifications),
        BottomNavItem("מועדפים", Routes.Favorites.route, Icons.Default.Favorite)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                navController = navController,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(item.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                unreadCount = unreadCount

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
                currentUserViewModel = currentUserViewModel,
                notificationsViewModel = notificationsViewModel
            )
        }
    }
}
