package com.example.soccergamesfinder.ui.components.game

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.screens.ChatScreen

@Composable
fun GameChatSection(
    gameId: String,
    userId: String
) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "💬 צ'אט המשחק",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    ChatScreen(gameId = gameId, userId = userId)
}
