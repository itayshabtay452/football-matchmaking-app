// FilterComponents.kt – גרסה מודרנית לכל קומפוננטות הסינון
package com.example.soccergamesfinder.ui.components.shared

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePickerFieldModern(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    label: String = "בחר תאריך",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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
            datePicker.minDate = System.currentTimeMillis()
        }
    }

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("$label: $selectedDate", style = MaterialTheme.typography.labelSmall)
    }
}
