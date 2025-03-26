package com.example.soccergamesfinder.ui.components.field

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game

@Composable
fun GameListSection(games: List<Game>, onGameClick: (String) -> Unit) {
    if (games.isEmpty()) {
        Text("❌ אין משחקים זמינים", color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(games) { game ->
                GameItem(game = game, onClick = onGameClick)
            }
        }
    }
}