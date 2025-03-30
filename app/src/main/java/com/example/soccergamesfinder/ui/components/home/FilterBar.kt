package com.example.soccergamesfinder.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.FieldFilterState


@Composable
fun FilterBar(
    filterState: FieldFilterState,
    onCityChanged: (String) -> Unit,
    onLightingChanged: (Boolean) -> Unit,
    onSizeChanged: (String?) -> Unit,
    onMaxDistanceChanged: (Double) -> Unit,
    onResetFilters: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("🔍 סינון מתקנים", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onResetFilters) {
                    Text("נקה")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = filterState.city ?: "",
                onValueChange = onCityChanged,
                label = { Text("עיר") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = filterState.lighting,
                    onCheckedChange = onLightingChanged
                )
                Text("תאורה קיימת")
            }

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }
            val sizes = listOf("קטן", "בינוני", "גדול")
            OutlinedButton(onClick = { expanded = true }) {
                Text("גודל: ${filterState.size ?: "הכל"}")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("כל הגדלים") }, onClick = { onSizeChanged(null); expanded = false })
                sizes.forEach { size ->
                    DropdownMenuItem(text = { Text(size) }, onClick = { onSizeChanged(size); expanded = false })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("מרחק מקסימלי: ${filterState.maxDistanceKm.toInt()} ק״מ")
            Slider(
                value = filterState.maxDistanceKm.toFloat(),
                onValueChange = { onMaxDistanceChanged(it.toDouble()) },
                valueRange = 1f..50f,
                steps = 49
            )
        }
    }
}


