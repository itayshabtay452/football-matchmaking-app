package com.example.soccergamesfinder.ui.components.inputs

import android.app.DatePickerDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun DatePickerField(date: String, onDateSelected: (String) -> Unit) {
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
