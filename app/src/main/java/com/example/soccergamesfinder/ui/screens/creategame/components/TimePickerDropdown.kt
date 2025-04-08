// TimePickerDropdown.kt
package com.example.soccergamesfinder.ui.screens.creategame.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun TimePickerDropdown(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    label: String = "בחר שעה",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val times = generateAllowedTimes()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selectedTime")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            times.forEach { time ->
                DropdownMenuItem(
                    text = { Text(time) },
                    onClick = {
                        onTimeSelected(time)
                        expanded = false
                    }
                )
            }
        }
    }
}

// יוצרת רשימת שעות חוקיות
@SuppressLint("DefaultLocale")
fun generateAllowedTimes(): List<String> {
    val allowed = mutableListOf<String>()
    for (hour in 8..23) {
        allowed.add(String.format("%02d:00", hour))
        allowed.add(String.format("%02d:30", hour))
    }
    return allowed
}
