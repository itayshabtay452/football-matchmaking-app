package com.example.soccergamesfinder.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Message
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatMessageBubble(message: Message) {
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val time = formatter.format(Date(message.timestamp))

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("${message.senderName} • $time", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(message.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
