// ui/components/game/GameHeaderSection.kt
package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameHeaderSection(game: Game, modifier: Modifier = Modifier) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val date = dateFormatter.format(game.startTime.toDate())
    val start = timeFormatter.format(game.startTime.toDate())
    val end = timeFormatter.format(game.endTime.toDate())
    val status = when (game.status) {
        GameStatus.OPEN -> "פתוח"
        GameStatus.FULL -> "מלא"
        GameStatus.ENDED -> "הסתיים"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text("🗓️ $date", style = MaterialTheme.typography.titleMedium)
        Text("🕓 $start - $end", style = MaterialTheme.typography.bodyMedium)
        Text("🔁 סטטוס: $status", style = MaterialTheme.typography.bodyMedium)
        Text("👥 ${game.joinedPlayers.size} מתוך ${game.maxPlayers}", style = MaterialTheme.typography.bodyMedium)
    }
}
