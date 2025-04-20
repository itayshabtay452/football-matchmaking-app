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
import com.example.soccergamesfinder.ui.components.chat.ChatSection
import com.example.soccergamesfinder.ui.screens.game.component.GameDescriptionSection
import com.example.soccergamesfinder.ui.screens.game.component.GameFieldCard
import com.example.soccergamesfinder.ui.screens.game.component.GameHeaderSection
import com.example.soccergamesfinder.ui.screens.game.component.GameParticipantsSection
import com.example.soccergamesfinder.viewmodel.chat.ChatViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    gameId: String,
    onNavigateToField: (String) -> Unit,
    onNavigateToUser: (String) -> Unit
) {
    val gameViewModel: GameViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()

    val state = gameViewModel.state.collectAsState().value
    val chatState = chatViewModel.state.collectAsState().value

    LaunchedEffect(gameId) {
        gameViewModel.startListening(gameId)
        chatViewModel.startListening(gameId)
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
                        item {
                            Divider()
                            ChatSection(
                                messages = chatState.messages,
                                currentMessage = chatState.currentMessage,
                                onMessageChange = chatViewModel::onMessageChange,
                                onSendClick = { chatViewModel.sendMessage(gameId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            )
                        }

                    }
                }
            }
        }
    }
}
