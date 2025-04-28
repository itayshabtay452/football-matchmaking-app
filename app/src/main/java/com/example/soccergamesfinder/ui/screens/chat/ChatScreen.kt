// ChatScreen.kt – מסך צ'אט ייעודי למשחק
package com.example.soccergamesfinder.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    gameId: String,
    viewModel: ChatViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(gameId) {
        viewModel.startListening(gameId)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("צ'אט המשחק") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "חזור")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = false
            ) {
                items(state.messages) { msg ->
                    val isCurrentUser = msg.senderId == state.currentUserId
                    ChatMessageBubble(
                        message = msg,
                        isCurrentUser = isCurrentUser
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.currentMessage,
                    onValueChange = viewModel::onMessageChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("הקלד הודעה...") }
                )
                IconButton(onClick = { viewModel.sendMessage(gameId) }) {
                    Icon(Icons.Default.Send, contentDescription = "שלח")
                }
            }
        }
    }
}
