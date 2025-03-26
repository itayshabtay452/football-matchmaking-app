package com.example.soccergamesfinder.ui.components.field

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game

@Composable
fun GameItem(game: Game, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📆 ${game.getFormattedStartTime()}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "👤 ${game.players.size}/${game.maxPlayers} משתתפים",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "📜 ${game.description ?: "ללא תיאור"}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onClick(game.id) }, modifier = Modifier.align(Alignment.End)) {
                Text("🔍 צפה במשחק")
            }
        }
    }
}