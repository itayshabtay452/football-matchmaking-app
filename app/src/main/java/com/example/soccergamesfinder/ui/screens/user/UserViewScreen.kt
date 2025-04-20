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
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.GameStatus
import com.example.soccergamesfinder.ui.components.GameCard

@Composable
fun UserViewScreen(
    userId: String,
    onNavigateToGame: (String) -> Unit
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
            val user = state.user
            val futureGames = state.games.filter { it.status != GameStatus.ENDED }
            val pastGames = state.games.filter { it.status == GameStatus.ENDED }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // 🔹 פרטי משתמש
                    Text("פרופיל משתמש", style = MaterialTheme.typography.headlineSmall)
                    user?.profileImageUrl?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "תמונת פרופיל",
                            modifier = Modifier.size(96.dp)
                        )
                    }
                    if (user != null) {
                        Text("שם: ${user.fullName}")
                    }
                    if (user != null) {
                        Text("כינוי: ${user.nickname}")
                    }
                }

                // 🔹 משחקים עתידיים
                if (futureGames.isNotEmpty()) {
                    item {
                        Text("משחקים עתידיים", style = MaterialTheme.typography.titleMedium)
                    }
                    items(futureGames.size) { index ->
                        val game = futureGames[index]
                        GameCard(
                            game = game,
                            showJoinButton = false,
                            showLeaveButton = false,
                            showDeleteButton = false,
                            onCardClick = { onNavigateToGame(game.id) }
                        )
                    }
                }

                // 🔹 היסטוריית משחקים
                if (pastGames.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("היסטוריית משחקים", style = MaterialTheme.typography.titleMedium)
                    }
                    items(pastGames.size) { index ->
                        val game = pastGames[index]
                        GameCard(
                            game = game,
                            showJoinButton = false,
                            showLeaveButton = false,
                            showDeleteButton = false,
                            onCardClick = { onNavigateToGame(game.id) }
                        )
                    }
                }
            }
        }
    }
}
