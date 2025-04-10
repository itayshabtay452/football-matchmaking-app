// DatePickerField.kt
package com.example.soccergamesfinder.ui.components.shared

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    label: String = "בחר תאריך"
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // פתיחת הדיאלוג
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val date = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                onDateSelected(formatter.format(date.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis() // רק תאריכים עתידיים
        }
    }

    OutlinedButton(onClick = { datePickerDialog.show() }) {
        Text("$label: $selectedDate")
    }
}
