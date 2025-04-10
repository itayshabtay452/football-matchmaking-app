// FieldCard.kt
package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldCard(
    field: Field,
    onClick: (() -> Unit)? = null,
    onCreateGameClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(field.name ?: "מגרש ללא שם", style = MaterialTheme.typography.titleMedium)
            Text(field.address ?: "כתובת לא זמינה", style = MaterialTheme.typography.bodySmall)
            Text(
                "מרחק: ${String.format("%.1f", field.distance ?: 0.0)} ק" + '"' + "מ",
                style = MaterialTheme.typography.bodySmall
            )
            Text("גודל: ${field.size ?: "לא צויין"}", style = MaterialTheme.typography.bodySmall)
            Text("תאורה: ${if (field.lighting) "כן" else "לא"}", style = MaterialTheme.typography.bodySmall)
            Text("משחקים פתוחים: ${field.games.size}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onCreateGameClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("צור משחק")
            }
        }
    }
}