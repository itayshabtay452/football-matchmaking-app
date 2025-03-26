package com.example.soccergamesfinder.ui.components.inputs

import android.app.TimePickerDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun TimePickerField(time: String, onTimeSelected: (String) -> Unit) {
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
