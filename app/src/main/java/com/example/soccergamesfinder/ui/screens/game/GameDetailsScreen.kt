// GameDetailsScreen.kt
package com.example.soccergamesfinder.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.screens.allgames.AllGamesViewModel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    gameId: String,
    gameListViewModel: GameListViewModel,
    fieldListViewModel: FieldListViewModel,
    currentUserViewModel: CurrentUserViewModel,
    onNavigateToField: (String) -> Unit,
    onNavigateToUser: (String) -> Unit
) {
    val gameViewModel: GameViewModel = hiltViewModel()
    val gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()

    val state = gameViewModel.state.collectAsState().value

    LaunchedEffect(gameId) {
        gameViewModel.startListening(gameId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("פרטי משחק") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.error != null -> {
                    Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
                }

                state.game != null -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            GameHeaderSection(game = state.game)
                        }

                        item {
                            GameDescriptionSection(
                                description = state.game.description
                            )
                        }
                        item {
                            GameParticipantsSection(
                                creator = state.creator,
                                participants = state.participants,
                                onUserClick = { user -> onNavigateToUser(user.id) }
                            )
                        }
                        item {
                            if (state.field != null) {
                                GameFieldCard(
                                    field = state.field,
                                    onClick = { onNavigateToField(state.field.id) }
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
