package com.example.soccergamesfinder.ui.components.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorMessage(message: String?, modifier: Modifier = Modifier) {
    if (!message.isNullOrEmpty()) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}
