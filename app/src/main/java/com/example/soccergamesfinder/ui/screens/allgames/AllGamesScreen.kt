package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.GameCarousel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllGamesScreen(
    onGameClick: (String) -> Unit,
    gameListViewModel: GameListViewModel,
    fieldListViewModel: FieldListViewModel,
    currentUserViewModel: CurrentUserViewModel
) {
    val allGamesViewModel: AllGamesViewModel = hiltViewModel()
    val gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()

    val gameState = gameListViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value
    val currentUserState = currentUserViewModel.state.collectAsState().value
    val state = allGamesViewModel.state.collectAsState().value

    LaunchedEffect(gameState.games, fieldState.fields) {
        allGamesViewModel.onDataChanged(gameState.games, fieldState.fields)
    }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    AllGamesFilterSection(
                        state = state,
                        onEvent = allGamesViewModel::onEvent
                    )
                }

                Text(
                    text = "🎯 נמצאו ${state.filteredGames.size} משחקים מתאימים",
                    style = MaterialTheme.typography.labelSmall
                )

                GameCarousel(
                    games = state.filteredGames,
                    fields = state.fields,
                    currentUser = currentUserState.user,
                    onJoinClick = { gameDetailsViewModel.joinGame(it) },
                    onLeaveClick = { gameDetailsViewModel.leaveGame(it) },
                    onDeleteClick = { gameDetailsViewModel.deleteGame(it) },
                    onCardClick = { onGameClick(it.id) }
                )
            }
        }
    }
}
