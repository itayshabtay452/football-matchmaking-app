package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.shared.*
import com.example.soccergamesfinder.ui.components.shared.DatePickerField
import com.example.soccergamesfinder.utils.GameFilterManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllGamesFilterSection(
    state: AllGamesState,
    onEvent: (AllGamesEvent) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // חיפוש לפי שם מגרש
        FilterTextField(
            label = "שם מגרש",
            value = state.fieldNameQuery,
            onValueChange = { onEvent(AllGamesEvent.FieldNameChanged(it)) }
        )

        // סינון לפי מצב משחק
        FilterDropdown(
            label = "מצב משחק",
            options = listOf(null) + GameStatusOption.entries,
            selectedOption = state.selectedStatus,
            onOptionSelected = { onEvent(AllGamesEvent.StatusChanged(it)) },
            optionToString = { it?.name ?: "הכל" }
        )

        // טווח תאריכים (תאריך התחלה וסיום)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DatePickerField(
                selectedDate = state.dateRange.first?.format(dateFormatter) ?: "לא נבחר",
                onDateSelected = { selected ->
                    val newStart = LocalDate.parse(selected, dateFormatter)
                    val currentEnd = state.dateRange.second // ⬅️ נשלף ברגע הלחיצה
                    onEvent(AllGamesEvent.DateRangeChanged(newStart, currentEnd))
                },
                label = "מתאריך"
            )
            DatePickerField(
                selectedDate = state.dateRange.second?.format(dateFormatter) ?: "לא נבחר",
                onDateSelected = { selected ->
                    val newEnd = LocalDate.parse(selected, dateFormatter)
                    val currentStart = state.dateRange.first // ⬅️ נשלף ברגע הלחיצה
                    onEvent(AllGamesEvent.DateRangeChanged(currentStart, newEnd))
                },
                label = "עד תאריך"
            )
        }

        Text("טווח שעות", style = MaterialTheme.typography.titleSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HourPickerDropdown(
                selectedHour = state.timeRange.first,
                onHourSelected = { newStart ->
                    onEvent(AllGamesEvent.TimeRangeChanged(newStart, state.timeRange.second))
                },
                label = "שעת התחלה",
                modifier = Modifier.weight(1f)
            )

            HourPickerDropdown(
                selectedHour = state.timeRange.second,
                onHourSelected = { newEnd ->
                    onEvent(AllGamesEvent.TimeRangeChanged(state.timeRange.first, newEnd))
                },
                label = "שעת סיום",
                modifier = Modifier.weight(1f)
            )
        }


        // מיון
        FilterDropdown(
            label = "מיון לפי",
            options = GameFilterManager.SortOption.entries,
            selectedOption = state.sortOption,
            onOptionSelected = { it?.let { it1 -> AllGamesEvent.SortOptionChanged(it1) }
                ?.let { it2 -> onEvent(it2) } }
        )

        // כפתור איפוס
        OutlinedButton(
            onClick = { onEvent(AllGamesEvent.ClearFilters) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("איפוס סינון")
        }
    }
}
