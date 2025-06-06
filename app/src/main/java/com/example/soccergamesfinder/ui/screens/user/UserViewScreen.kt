// 📁 ui/screens/user/UserViewScreen.kt
package com.example.soccergamesfinder.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.model.GameStatus

@Composable
fun UserViewScreen(
    userId: String,
    onNavigateToGame: (String) -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val viewModel = hiltViewModel<UserViewViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        state.user != null -> {
            val futureGames = state.games.filter { it.status == GameStatus.OPEN }
            val user = state.user

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    if (user != null) {
                        UserProfileHeader(user)
                    }
                }

                item {
                    UserStatisticsSection(
                        totalGames = state.games.size + state.pastGames.size,
                        futureGames = state.games.size,
                        pastGames = state.pastGames.size
                    )
                }

                if (futureGames.isNotEmpty()) {
                    item {
                        UserGamesSection(
                            title = "משחקים עתידיים",
                            games = futureGames,
                            onGameClick = { game -> onNavigateToGame(game.id) }
                        )
                    }
                }

                if (state.isOwnProfile) {
                    item {
                        Button(
                            onClick = { onNavigateToEditProfile() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ערוך פרופיל")
                        }
                    }
                }



            }
        }
    }
}
