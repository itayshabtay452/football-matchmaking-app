package com.example.soccergamesfinder.navigation

/**
 * Centralized definition of all navigation routes in the app.
 *
 * Each object represents a screen or navigation graph destination,
 * and holds the unique route string associated with it.
 *
 * This structure allows for type-safe and consistent navigation throughout the app.
 */
sealed class Routes(val route: String) {

    // Navigation graphs
    object AuthGraph : Routes("auth_graph")
    object MainGraph : Routes("main_graph")

    // Authentication screens
    object Login : Routes("login")
    object CompleteProfile : Routes("complete_profile")

    // Main screens
    object Home : Routes("home")
    object AllFields : Routes("all_fields")
    object AllGames : Routes("all_games")
    object UserProfile : Routes("user_profile")
    object Field : Routes("fieldScreen")
    object Game : Routes("gameScreen")

    // Additional features
    object AddField : Routes("add_field")
    object Notifications : Routes("notifications")
    object Chat : Routes("chat")
    object Favorites : Routes("favorites")
    object EditProfile : Routes("edit_profile")
}
