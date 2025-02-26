package com.example.soccergamesfinder.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun showDatePicker(context: Context, calendar: Calendar, onDateSelected: (String) -> Unit) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(sdf.format(selectedCalendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}
