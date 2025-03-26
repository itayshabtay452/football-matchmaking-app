package com.example.soccergamesfinder.ui.components.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.inputs.DatePickerField
import com.example.soccergamesfinder.ui.components.inputs.TimePickerField
import com.example.soccergamesfinder.utils.ValidationResult

@Composable
fun GameDetailsForm(
    selectedDate: String,
    selectedStartTime: String,
    selectedEndTime: String,
    maxPlayers: String,
    description: String,
    startTimeValidation: ValidationResult,
    endTimeValidation: ValidationResult,
    onDateChange: (String) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onMaxPlayersChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    DatePickerField(date = selectedDate, onDateSelected = onDateChange)

    TimePickerField(time = selectedStartTime, onTimeSelected = onStartTimeChange)
    TimePickerField(time = selectedEndTime, onTimeSelected = onEndTimeChange)

    OutlinedTextField(
        value = maxPlayers,
        onValueChange = { onMaxPlayersChange(it.filter { char -> char.isDigit() }) },
        label = { Text("👥 מספר מקסימלי של משתתפים") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("📜 תיאור המשחק (אופציונלי)") },
        modifier = Modifier.fillMaxWidth()
    )

    if (startTimeValidation is ValidationResult.Error) {
        Text(text = startTimeValidation.message, color = MaterialTheme.colorScheme.error)
    }
    if (endTimeValidation is ValidationResult.Error) {
        Text(text = endTimeValidation.message, color = MaterialTheme.colorScheme.error)
    }
}
