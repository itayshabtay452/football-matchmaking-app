package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
    onLightingChanged: (Boolean) -> Unit,
    onParkingChanged: (Boolean) -> Unit,
    onFencingChanged: (Boolean) -> Unit,
    onNameQueryChanged: (String) -> Unit,
    onSizeChanged: (String?) -> Unit,
    onMaxDistanceChanged: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("🔍 סינון מתקנים", style = MaterialTheme.typography.titleMedium)

        Row {
            Checkbox(
                checked = filterState.lighting,
                onCheckedChange = onLightingChanged
            )
            Text("תאורה קיימת")
        }

        Row {
            Checkbox(
                checked = filterState.parking,
                onCheckedChange = onParkingChanged
            )
            Text("חניה לרכבים")
        }

        Row {
            Checkbox(
                checked = filterState.fencing,
                onCheckedChange = onFencingChanged
            )
            Text("גידור קיים")
        }

        OutlinedTextField(
            value = filterState.nameQuery,
            onValueChange = onNameQueryChanged,
            label = { Text("חיפוש לפי שם") },
            modifier = Modifier.fillMaxWidth()
        )

        // תפריט לבחירת גודל
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

        OutlinedTextField(
            value = filterState.maxDistanceKm?.toString() ?: "",
            onValueChange = onMaxDistanceChanged,
            label = { Text("מרחק מקסימלי (ק״מ)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
