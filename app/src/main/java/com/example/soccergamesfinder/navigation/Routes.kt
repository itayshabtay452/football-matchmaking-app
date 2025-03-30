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
    object Notifications : Routes("notifications")
    object UserHistory : Routes("user_history")
    object AddField : Routes("add_field")
    object Field : Routes("fieldScreen")
    object CreateGame : Routes("createGame")
    object Game : Routes("gameScreen")
}
