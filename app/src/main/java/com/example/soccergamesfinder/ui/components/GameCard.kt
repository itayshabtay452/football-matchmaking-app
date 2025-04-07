// GameCard.kt
package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameCard(
    game: Game,
    currentUser: User?,
    onJoinClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val isParticipant = currentUser?.id in game.joinedPlayers
    val isCreator = currentUser?.id == game.creatorId
    val isFull = game.joinedPlayers.size >= game.maxPlayers

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy, E", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val dateStr = dateFormatter.format(game.startTime.toDate())
    val startStr = timeFormatter.format(game.startTime.toDate())
    val endStr = timeFormatter.format(game.endTime.toDate())

    val fieldName = game.field?.name ?: "מגרש לא ידוע"
    val fieldAddress = game.field?.address ?: "כתובת לא זמינה"

    Card(
        modifier = Modifier
            .width(260.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(fieldName, style = MaterialTheme.typography.titleMedium)
                Text(fieldAddress, style = MaterialTheme.typography.bodySmall)
                Text("$dateStr | $startStr - $endStr", style = MaterialTheme.typography.bodySmall)
                Text("יוצר: ${game.creatorName ?: "לא ידוע"}", style = MaterialTheme.typography.bodySmall)
                Text("שחקנים: ${game.joinedPlayers.size}/${game.maxPlayers}", style = MaterialTheme.typography.bodySmall)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when {
                    !isParticipant && !isFull -> {
                        Button(onClick = onJoinClick, modifier = Modifier.weight(1f)) {
                            Text("הצטרף")
                        }
                    }
                    isParticipant && !isCreator -> {
                        OutlinedButton(onClick = onLeaveClick, modifier = Modifier.weight(1f)) {
                            Text("עזוב")
                        }
                        Button(onClick = onChatClick, modifier = Modifier.weight(1f)) {
                            Text("צ'אט")
                        }
                    }
                    isCreator -> {
                        OutlinedButton(onClick = onDeleteClick, modifier = Modifier.weight(1f)) {
                            Text("מחק")
                        }
                        Button(onClick = onChatClick, modifier = Modifier.weight(1f)) {
                            Text("צ'אט")
                        }
                    }
                }
            }
        }
    }
}
