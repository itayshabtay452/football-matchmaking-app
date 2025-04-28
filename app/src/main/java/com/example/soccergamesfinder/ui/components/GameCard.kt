// GameCard.kt – תואם לחלוטין ל־FieldCard בגודל וסגנון

package com.example.soccergamesfinder.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.R
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

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val dateStr = dateFormatter.format(game.startTime.toDate())
    val startStr = timeFormatter.format(game.startTime.toDate())
    val endStr = timeFormatter.format(game.endTime.toDate())

    val fieldName = field?.name ?: game.fieldName ?: "מגרש לא ידוע"
    val creatorName = game.creatorName ?: "לא ידוע"
    val canOpenGame = showJoinButton || showLeaveButton || showDeleteButton || showChatButton

    ElevatedCard(
        modifier = Modifier
            .width(182.dp)
            .wrapContentHeight()
            .clickable(enabled = canOpenGame) {
                if (canOpenGame) {
                    onCardClick?.invoke()
                } else {
                    Toast.makeText(context, "רק משתתפים יכולים לצפות בפרטי המשחק", Toast.LENGTH_SHORT).show()
                }
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.game),
                contentDescription = "תמונה של משחק",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = fieldName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    GameRow(Icons.Outlined.CalendarToday, dateStr)
                    GameRow(Icons.Outlined.AccessTime, "$startStr - $endStr")
                }

                Spacer(modifier = Modifier.height(2.dp))

                if (canOpenGame && onCardClick != null) {
                    TextButton(
                        onClick = { onCardClick() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("פרטים על המשחק", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                if (showJoinButton || showLeaveButton || showDeleteButton) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (showJoinButton && onJoinClick != null) {
                            Button(
                                onClick = onJoinClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("הצטרף")
                            }
                        }

                        if (showLeaveButton && onLeaveClick != null) {
                            OutlinedButton(
                                onClick = onLeaveClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("עזוב")
                            }
                        }
                    }

                    if (showDeleteButton && onDeleteClick != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("מחק")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
