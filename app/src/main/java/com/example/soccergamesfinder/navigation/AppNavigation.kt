package com.example.soccergamesfinder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
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
