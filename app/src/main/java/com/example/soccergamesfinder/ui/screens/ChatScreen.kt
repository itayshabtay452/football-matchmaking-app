package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.data.ChatMessage
import com.example.soccergamesfinder.viewmodel.ChatViewModel
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    gameId: String,
    navController: NavController,
    userViewModel: UserViewModel, // נוסיף את UserViewModel כפרמטר
    chatViewModel: ChatViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
){
    val messages by chatViewModel.messages.collectAsState()
    val user = authViewModel.user.value
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(gameId) {
        chatViewModel.loadMessages(gameId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("צ'אט המשחק") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "חזור")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                reverseLayout = false // הודעות חדשות יהיו למטה
            ) {
                items(messages) { message ->
                    MessageBubble(message, user?.uid == message.senderId, chatViewModel)
                }
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("הקלד הודעה...") }
            )

            Button(
                onClick = {
                    user?.let {
                        userViewModel.getUserName(it.uid) { userName ->
                            val finalName = userName ?: "שחקן"
                            chatViewModel.sendMessage(gameId, it.uid, finalName, messageText)
                            messageText = "" // איפוס הטקסט לאחר השליחה
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = messageText.isNotBlank()
            ) {
                Text("שלח")
            }


        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, isUserMessage: Boolean, chatViewModel: ChatViewModel) {
    val formattedTime = chatViewModel.formatTimestamp(message.timestamp) // המרת timestamp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.padding(4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(message.senderName, fontWeight = FontWeight.Bold)
                Text(message.message)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formattedTime,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End) // יישור תאריך לשמאל/ימין בהתאם להודעה
                )
            }
        }
    }
}

