// GameDetailsScreen.kt
package com.example.soccergamesfinder.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.screens.game.component.GameTopSection
import com.example.soccergamesfinder.ui.screens.game.component.ParticipantsRow
import com.example.soccergamesfinder.ui.screens.chat.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    gameId: String,
    onNavigateToField: (String) -> Unit,
    onNavigateToUser: (String) -> Unit,
    onNavigateToChat: (String) -> Unit
) {
    val gameViewModel: GameViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()

    val state = gameViewModel.state.collectAsState().value
    val chatState = chatViewModel.state.collectAsState().value

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
                            state.field?.let {
                                GameTopSection(
                                    game = state.game,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    onFieldClick = { onNavigateToField(state.field.id) },
                                    field = it
                                )
                            }
                        }

                        item {
                            ParticipantsRow(
                                creator = state.creator,
                                participants = state.participants,
                                onUserClick = { user -> onNavigateToUser(user.id) }
                            )
                        }

                        item {
                            Button(
                                onClick = { onNavigateToChat(gameId) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("עבור לצ'אט המשחק")
                            }
                        }


                    }

                    }
                }
            }
        }
    }
