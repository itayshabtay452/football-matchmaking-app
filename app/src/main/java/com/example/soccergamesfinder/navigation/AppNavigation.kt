package com.example.soccergamesfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AuthGraph.route
    ) {
        // טוען את גרף האימות (Login, Complete Profile)
        authNavGraph(navController)
        // טוען את הגרף הראשי (Home, מסכים נוספים)
        mainNavGraph(navController)
    }
}
