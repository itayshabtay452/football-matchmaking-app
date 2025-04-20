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
import com.example.soccergamesfinder.ui.components.GameList
import com.example.soccergamesfinder.ui.screens.creategame.CreateGameDialog
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
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
                    // מידע על המגרש
                    Text(field.name ?: "מגרש ללא שם", style = MaterialTheme.typography.headlineSmall)
                    Text("כתובת: ${field.address ?: "לא צוינה"}")
                    Text("גודל: ${field.size ?: "לא צויין"}")
                    Text("תאורה: ${if (field.lighting) "כן" else "לא"}")
                    Text("מרחק: ${String.format("%.1f", field.distance ?: 0.0)} ק\"מ")

                    Spacer(modifier = Modifier.height(12.dp))

                    field.latitude?.let { lat ->
                        field.longitude?.let { lng ->
                            FieldMap(
                                latitude = lat,
                                longitude = lng,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // כפתור ליצירת משחק חדש
                    Button(
                        onClick = {  openDialog.value = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("צור משחק")
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("משחקים במגרש זה:", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    FieldStatisticsSection(
                        totalGames = fieldState.totalGames,
                        avgPlayers = fieldState.avgPlayers,
                        fullGames = fieldState.fullGames,
                        openGames = fieldState.openGames
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    GameList(
                        games = fieldGames,
                        currentUser = currentUser,
                        fields = listOf(field),
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
                            onNavigateToGame(game.id)
                        }
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

