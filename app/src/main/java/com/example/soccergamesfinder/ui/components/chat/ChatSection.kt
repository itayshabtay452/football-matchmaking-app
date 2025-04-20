package com.example.soccergamesfinder.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Message

@Composable
fun ChatSection(
    messages: List<Message>,
    currentMessage: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatMessageBubble(message)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("כתוב הודעה...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onSendClick, enabled = currentMessage.isNotBlank()) {
                Text("שלח")
            }
        }
    }
}
