package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameHeaderCard(game: Game, modifier: Modifier = Modifier) {
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

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            GameInfoRow(Icons.Default.CalendarToday, date)
            GameInfoRow(Icons.Default.Schedule, "$start - $end")
            GameInfoRow(Icons.Default.Sync, "סטטוס: $status")
            GameInfoRow(Icons.Default.Groups, "שחקנים: ${game.joinedPlayers.size}/${game.maxPlayers}")
        }
    }
}

@Composable
private fun GameInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
