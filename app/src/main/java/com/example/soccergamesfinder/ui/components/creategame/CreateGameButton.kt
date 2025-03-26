package com.example.soccergamesfinder.ui.components.creategame

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateGameButton(
    enabled: Boolean,
    errorMessage: String?,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = onClick, enabled = enabled) {
        Text("🎮 צור משחק")
    }

    errorMessage?.takeIf { it.isNotBlank() }?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
