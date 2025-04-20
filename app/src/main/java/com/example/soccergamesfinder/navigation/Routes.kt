package com.example.soccergamesfinder.navigation

sealed class Routes(val route: String) {
    object AuthGraph : Routes("auth_graph")
    object MainGraph : Routes("main_graph")

    object Login : Routes("login")
    object CompleteProfile : Routes("complete_profile")
    object Home : Routes("home")
    object AllFields : Routes("all_fields")
    object AllGames : Routes("all_games")
    object EditProfile : Routes("edit_profile")
    object UserProfile : Routes("user_profile")
    object Field : Routes("fieldScreen")
    object Game : Routes("gameScreen")
}
