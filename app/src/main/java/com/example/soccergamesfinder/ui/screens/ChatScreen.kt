package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.ChatMessage
import com.example.soccergamesfinder.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(gameId: String, userId: String, viewModel: ChatViewModel = hiltViewModel()) {

    val messages by viewModel.massages.collectAsState()
    val isSendingMessage by viewModel.isSendingMessage.collectAsState()

    var newMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadMassages(gameId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        LazyColumn(modifier = Modifier.weight(1f), reverseLayout = true) {
            items(messages.reversed()) { message ->
                ChatMessageItem(message = message, isCurrentUser = message.senderId == userId)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row{
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("הקלד הודעה...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newMessage.isNotBlank()) {
                        val messageText = ChatMessage(
                            senderId = userId,
                            message = newMessage
                        )
                        coroutineScope.launch {
                            viewModel.sendMessage(gameId, messageText)
                            newMessage = ""
                        }
                    }
                },
                enabled = !isSendingMessage
            )
            {
                Text("שלח")
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, isCurrentUser: Boolean) {

    val backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val alignment = if (isCurrentUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = backgroundColor,
            tonalElevation = 2.dp
        ) {
            Text(
                text = message.message,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


