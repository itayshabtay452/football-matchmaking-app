package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdvancedFilterHeader(
    modifier: Modifier = Modifier,
    title: String = "סינון מתקדם",
    isFilterVisible: Boolean,
    onToggleFilter: () -> Unit,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onClearFilters) {
                Text("איפוס")
            }
            Button(onClick = onToggleFilter) {
                Text(if (isFilterVisible) "הסתר" else "הצג")
            }
        }
    }
}
