package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field

@Composable
fun GameFieldCard(
    field: Field,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🏟️ ${field.name ?: "מגרש ללא שם"}", style = MaterialTheme.typography.titleMedium)
            Text("📍 ${field.address ?: "כתובת לא ידועה"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
