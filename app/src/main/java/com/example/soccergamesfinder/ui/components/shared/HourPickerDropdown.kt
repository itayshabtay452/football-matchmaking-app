package com.example.soccergamesfinder.ui.components.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun HourPickerDropdown(
    selectedHour: Int?,
    onHourSelected: (Int?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val options = (8..23).toList()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: ${selectedHour?.let { String.format("%02d:00", it) } ?: "לא נבחר"}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("ללא סינון") },
                onClick = {
                    onHourSelected(null)
                    expanded = false
                }
            )
            options.forEach { hour ->
                val labelText = String.format("%02d:00", hour)
                DropdownMenuItem(
                    text = { Text(labelText) },
                    onClick = {
                        onHourSelected(hour)
                        expanded = false
                    }
                )
            }
        }
    }
}
