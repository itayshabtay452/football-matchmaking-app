package com.example.soccergamesfinder.ui.screens.creategame.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EndTimePickerDropdown(
    startTime: String,
    selectedEndTime: String,
    onEndTimeSelected: (String) -> Unit,
    label: String = "שעת סיום",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val options = remember(startTime) {
        generateEndTimesFromStart(startTime)
    }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selectedEndTime")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { time ->
                DropdownMenuItem(
                    text = { Text(time) },
                    onClick = {
                        onEndTimeSelected(time)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun generateEndTimesFromStart(startTime: String): List<String> {
    if (startTime.isBlank()) return emptyList()

    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val baseTime = formatter.parse(startTime) ?: return emptyList()

    val result = mutableListOf<String>()
    val calendar = Calendar.getInstance()

    for (offsetMinutes in 120..240 step 30) {
        calendar.time = baseTime
        calendar.add(Calendar.MINUTE, offsetMinutes)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        if (hour in 8..23) { // רק אם עדיין בטווח שעות לגיטימיות
            result.add(formatter.format(calendar.time))
        }
    }

    return result
}

