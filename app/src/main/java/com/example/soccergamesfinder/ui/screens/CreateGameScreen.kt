package com.example.soccergamesfinder.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.GameViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel
import com.example.soccergamesfinder.viewmodel.GameValidationViewModel
import java.util.Calendar

@SuppressLint("SuspiciousIndentation")
@Composable
fun CreateGameScreen(fieldId: String, userViewModel: UserViewModel, navigateBack: () -> Unit){

    val gameViewModel: GameViewModel = hiltViewModel()
    val gameValidationViewModel: GameValidationViewModel = hiltViewModel()
    val userId = userViewModel.getId()
    val startTimeValidation by gameValidationViewModel.startTimeValidation.collectAsState()
    val endTimeValidation  by gameValidationViewModel.endTimeValidation.collectAsState()
    val validStartTime by gameValidationViewModel.validStartTime.collectAsState()
    val validEndTime by gameValidationViewModel.validEndTime.collectAsState()

    var selectedDate by remember { mutableStateOf("") }
    var selectedStartTime by remember { mutableStateOf("") }
    var selectedEndTime by remember { mutableStateOf("10") }
    var maxPlayers by remember { mutableStateOf("10") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("🆕 יצירת משחק חדש", style = MaterialTheme.typography.headlineMedium)
        Text("📍 מגרש: $fieldId", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(label = "📅 תאריך", date = selectedDate) { selectedDate = it }
        TimePickerField(label = "⏰ שעת התחלה", time = selectedStartTime) {
            selectedStartTime = it
            gameValidationViewModel.validateStartTime(selectedDate, it)
        }
        TimePickerField(label = "⏳ שעת סיום", time = selectedEndTime) {
            selectedEndTime = it
            gameValidationViewModel.validateEndTime(selectedDate, selectedStartTime, it)
        }

        OutlinedTextField(
            value = maxPlayers,
            onValueChange = { maxPlayers = it.filter { char -> char.isDigit() } },
            label = { Text("👥 מספר מקסימלי של משתתפים") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("📜 תיאור המשחק (אופציונלי)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (startTimeValidation is ValidationResult.Error) {
            Text(text = (startTimeValidation as ValidationResult.Error).message, color = MaterialTheme.colorScheme.error)
        }
        if (endTimeValidation is ValidationResult.Error) {
            Text(text = (endTimeValidation as ValidationResult.Error).message, color = MaterialTheme.colorScheme.error)
        }


        Button(
            onClick = {
                val game =
                    Game(
                        fieldId = fieldId,
                        startTime = validStartTime!!,
                        endTime = validEndTime!!,
                        creatorId = userId!!,
                        maxPlayers = maxPlayers.toIntOrNull() ?: 10, // ברירת מחדל ל-10 משתתפים אם הערך אינו תקין
                        description = if (description.isNotBlank()) description else null // תיאור אופציונלי
                    )
                    gameViewModel.createGame(game)
                    navigateBack()
            },
            enabled = validStartTime != null && validEndTime != null
        ) {
            Text("🎮 צור משחק")
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimePickerField(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            onTimeSelected(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    OutlinedButton(onClick = { timePickerDialog.show() }) {
        Text(text = if (time.isNotEmpty()) "⏰ $time" else "⏰ בחר שעה")
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerField(label: String, date: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedButton(onClick = { datePickerDialog.show() }) {
        Text(text = if (date.isNotEmpty()) "📅 $date" else "📅 בחר תאריך")
    }
}



