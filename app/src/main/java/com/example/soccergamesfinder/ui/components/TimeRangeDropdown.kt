package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeRangeDropdown(selectedTimeRange: String, onTimeRangeSelected: (String) -> Unit) {
    val timeRanges = listOf("16:00-18:00", "18:00-20:00", "20:00-22:00", "22:00-24:00")
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedTimeRange.isEmpty()) "בחר טווח שעות" else "שעות שנבחרו: $selectedTimeRange")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            timeRanges.forEach { range ->
                DropdownMenuItem(
                    text = { Text(range) },
                    onClick = {
                        onTimeRangeSelected(range)
                        expanded = false
                    }
                )
            }
        }
    }
}
