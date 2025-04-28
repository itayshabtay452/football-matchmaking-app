package com.example.soccergamesfinder.ui.screens.field

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.GameCarousel
import com.example.soccergamesfinder.ui.screens.creategame.CreateGameDialog
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@SuppressLint("DefaultLocale")
@Composable
fun FieldDetailsScreen(
    fieldId: String,
    currentUserViewModel: CurrentUserViewModel = hiltViewModel(),
    fieldDetailsViewModel: FieldDetailsViewModel = hiltViewModel(),
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel(),
    onNavigateToGame: (String) -> Unit
) {
    val fieldState = fieldDetailsViewModel.state.collectAsState().value
    val userState = currentUserViewModel.state.collectAsState().value
    val currentUser = userState.user

    val openDialog = remember { mutableStateOf(false) }
    LaunchedEffect(fieldId) {
        fieldDetailsViewModel.startListening(fieldId)
    }


    when {
        fieldState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        fieldState.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("שגיאה: ${fieldState.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        fieldState.field != null -> {
            val field = fieldState.field
            val fieldGames = fieldState.games

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    FieldHeaderSection(field)
                }

                item {
                    FieldStatsAndActionSection(
                        totalGames = fieldState.totalGames,
                        avgPlayers = fieldState.avgPlayers,
                        onAddGameClick = {
                            openDialog.value = true
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    GameCarousel(
                        games = fieldGames,
                        fields = listOf(field),
                        currentUser = currentUser,
                        onJoinClick = { gameDetailsViewModel.joinGame(it) },
                        onLeaveClick = { gameDetailsViewModel.leaveGame(it) },
                        onDeleteClick = { gameDetailsViewModel.deleteGame(it) },
                        onCardClick = { game -> onNavigateToGame(game.id) }
                    )
                }
            }
            if (openDialog.value) {
                CreateGameDialog(
                    field = fieldState.field,
                    onDismiss = { openDialog.value = false },
                    onCreateSuccess = { game ->
                        openDialog.value = false
                    }
                )
            }
        }
    }
}

