package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.GameList
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllGamesScreen(
    onGameClick: (String) -> Unit,
    gameListViewModel: GameListViewModel = hiltViewModel(),
    fieldListViewModel: FieldListViewModel = hiltViewModel(),
    currentUserViewModel: CurrentUserViewModel = hiltViewModel()
) {
    val allGamesViewModel: AllGamesViewModel = hiltViewModel()
    val gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()

    val gameState = gameListViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value
    val currentUserState = currentUserViewModel.state.collectAsState().value

    val state = allGamesViewModel.state.collectAsState().value

    // אתחול ראשוני
    LaunchedEffect(gameState.games, fieldState.fields) {
        allGamesViewModel.onDataChanged(gameState.games, fieldState.fields)
    }

    // UI ראשי
    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "סינון ומיון משחקים",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    AllGamesFilterSection(state = state, onEvent = allGamesViewModel::onEvent)
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "נמצאו ${state.filteredGames.size} משחקים מתאימים",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    GameList(
                        games = state.filteredGames,
                        currentUser = currentUserState.user,
                        fields = state.fields,
                        onJoinClick = { game ->
                            gameDetailsViewModel.joinGame(game)
                        },
                        onLeaveClick = { game ->
                            gameDetailsViewModel.leaveGame(game)
                        },
                        onDeleteClick = { game ->
                            gameDetailsViewModel.deleteGame(game)
                        },
                        onCardClick = { game ->
                            onGameClick(game.id)
                        }
                    )
                }
            }
        }
    }
}
