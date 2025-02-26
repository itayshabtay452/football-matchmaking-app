package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.viewmodel.GameWithFieldName

@Composable
fun GameCard(
    gameWithFieldName: GameWithFieldName,
    userId: String,
    onJoinClick: (String, String) -> Unit,
    onLeaveClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    val game = gameWithFieldName.game
    val fieldName = gameWithFieldName.fieldName
    val createdByUserName = gameWithFieldName.createdByUserName
    val distance = gameWithFieldName.distanceFromUser
    val isUserCreator = game.createdByUserId == userId
    val isUserInGame = game.players.contains(userId)
    val playersCount = game.players.size
    val maxPlayers = game.maxPlayers
    val spotsLeft = maxPlayers - playersCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("מגרש: $fieldName", style = MaterialTheme.typography.titleLarge)
            Text("נפתח על ידי: $createdByUserName", style = MaterialTheme.typography.bodyMedium)
            Text("תאריך: ${game.date}", style = MaterialTheme.typography.bodyLarge)
            Text("שעות: ${game.timeRange}", style = MaterialTheme.typography.bodyLarge)
            Text("שחקנים: $playersCount/$maxPlayers", style = MaterialTheme.typography.bodyLarge)
            distance?.let {
                Text("מרחק: %.1f ק\"מ".format(it), style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isUserCreator) {
                Button(
                    onClick = { onDeleteClick(game.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("מחק משחק", color = Color.White)
                }
            } else {
                if (isUserInGame) {
                    Button(
                        onClick = { onLeaveClick(game.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("עזוב משחק", color = Color.White)
                    }
                } else if (spotsLeft > 0) {
                    Button(
                        onClick = { onJoinClick(game.id, game.date) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("הצטרף למשחק")
                    }
                } else {
                    Text("המשחק מלא!", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
