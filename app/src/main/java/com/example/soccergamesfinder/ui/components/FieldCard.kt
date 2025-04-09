package com.example.soccergamesfinder.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.ui.screens.creategame.CreateGameDialog
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel

@SuppressLint("DefaultLocale")
@Composable
fun FieldCard(
    field: Field,
    onViewGamesClick: () -> Unit,
    fieldListViewModel: FieldListViewModel,
    gameListViewModel: GameListViewModel
) {
    var showDialog by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .width(220.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(field.name ?: "מגרש ללא שם", style = MaterialTheme.typography.titleMedium)
                Text(field.address ?: "כתובת לא זמינה", style = MaterialTheme.typography.bodySmall)
                Text("מרחק: ${String.format("%.1f", field.distance ?: 0.0)} ק" + '"' + "מ", style = MaterialTheme.typography.bodySmall)
                Text("גודל: ${field.size ?: "לא צויין"}", style = MaterialTheme.typography.bodySmall)
                Text("תאורה: ${if (field.lighting) "כן" else "לא"}", style = MaterialTheme.typography.bodySmall)
                Text("משחקים פתוחים: ${field.games.size}", style = MaterialTheme.typography.bodySmall)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewGamesClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("צפייה")
                }
                Button(
                    onClick = {showDialog = true},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("צור משחק")
                }
            }
        }
    }
    if (showDialog) {
        CreateGameDialog(
            field = field,
            onDismiss = { showDialog = false },
            fieldListViewModel = fieldListViewModel,
            gameListViewModel = gameListViewModel
        )
    }
}
