package com.example.soccergamesfinder.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameCard(
    game: Game,
    field: Field? = null,
    showJoinButton: Boolean = false,
    showLeaveButton: Boolean = false,
    showDeleteButton: Boolean = false,
    showChatButton: Boolean = false,
    onJoinClick: (() -> Unit)? = null,
    onLeaveClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onCardClick: (() -> Unit)? = null
) {
    val context = LocalContext.current


    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy, E", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val dateStr = dateFormatter.format(game.startTime.toDate())
    val startStr = timeFormatter.format(game.startTime.toDate())
    val endStr = timeFormatter.format(game.endTime.toDate())

    val fieldName = field?.name ?: game.fieldName ?: "מגרש לא ידוע"
    val fieldAddress = field?.address ?: game.fieldAddress ?: "כתובת לא זמינה"
    val creatorName = game.creatorName ?: "לא ידוע"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (showJoinButton || showLeaveButton || showDeleteButton) {
                    onCardClick?.invoke()
                } else {
                    Toast
                        .makeText(context, "רק משתתפים יכולים לצפות בצ'אט", Toast.LENGTH_SHORT)
                        .show()
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(fieldName, style = MaterialTheme.typography.titleMedium)
            Text(fieldAddress, style = MaterialTheme.typography.bodySmall)
            Text("$dateStr | $startStr - $endStr", style = MaterialTheme.typography.bodySmall)
            Text("יוצר: $creatorName", style = MaterialTheme.typography.bodySmall)
            Text("שחקנים: ${game.joinedPlayers.size}/${game.maxPlayers}", style = MaterialTheme.typography.bodySmall)

            if (showJoinButton || showLeaveButton || showDeleteButton || showChatButton) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (showJoinButton && onJoinClick != null) {
                        Button(onClick = onJoinClick, modifier = Modifier.weight(1f)) {
                            Text("הצטרף")
                        }
                    }

                    if (showLeaveButton && onLeaveClick != null) {
                        OutlinedButton(onClick = onLeaveClick, modifier = Modifier.weight(1f)) {
                            Text("עזוב")
                        }
                    }

                    if (showDeleteButton && onDeleteClick != null) {
                        OutlinedButton(onClick = onDeleteClick, modifier = Modifier.weight(1f)) {
                            Text("מחק")
                        }
                    }
                }
            }
        }
    }
}
