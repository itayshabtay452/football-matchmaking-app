package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// כאן תוכל להוסיף ייבוא ל-ViewModels אם תזדקק להם בהמשך

object DemoScreens {


    @Composable
    fun AllGamesScreen(navigateToGame: (String) -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { navigateToGame("demo_game") }) {
                Text("All Games Screen")
            }
        }
    }

    @Composable
    fun EditProfileScreen(navigateBack: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateBack) {
                Text("Edit Profile Screen - חזור")
            }
        }
    }

    @Composable
    fun NotificationsScreen(navigateBack: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateBack) {
                Text("Notifications Screen - חזור")
            }
        }
    }

    @Composable
    fun UserHistoryScreen(navigateBack: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateBack) {
                Text("User History Screen - חזור")
            }
        }
    }

    @Composable
    fun AddFieldScreen(navigateBack: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateBack) {
                Text("Add Field Screen - חזור")
            }
        }
    }

    @Composable
    fun FieldScreen(
        fieldId: String,
        navigateToGame: (String) -> Unit
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        }
    }

    @Composable
    fun GameScreen(
        gameId: String,
        navigateBack: () -> Unit
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateBack) {
                Text("Game Screen for $gameId - חזור")
            }
        }
    }
}
