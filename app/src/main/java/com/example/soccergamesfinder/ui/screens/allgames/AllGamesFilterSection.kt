// AllGamesFilterSection.kt – גרסה מעודכנת עם שורת כפתורים ואייקונים

package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.shared.*
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
        FilterTextFieldModern(
            label = "שם מגרש",
            value = state.fieldNameQuery,
            onValueChange = { onEvent(AllGamesEvent.FieldNameChanged(it)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        )

        // סינון לפי מצב משחק
        FilterDropdownModern(
            label = "מצב משחק",
            options = listOf(null) + GameStatusOption.entries,
            selectedOption = state.selectedStatus,
            onOptionSelected = { onEvent(AllGamesEvent.StatusChanged(it)) },
            optionToString = { it?.name ?: "הכל" },
            leadingIcon = {
                Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        )

        // טווח תאריכים (תאריך התחלה וסיום)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DatePickerFieldModern(
                selectedDate = state.dateRange.first?.format(dateFormatter) ?: "לא נבחר",
                onDateSelected = { selected ->
                    val newStart = LocalDate.parse(selected, dateFormatter)
                    val currentEnd = state.dateRange.second
                    onEvent(AllGamesEvent.DateRangeChanged(newStart, currentEnd))
                },
                label = "מתאריך",
                modifier = Modifier.weight(1f)
            )
            DatePickerFieldModern(
                selectedDate = state.dateRange.second?.format(dateFormatter) ?: "לא נבחר",
                onDateSelected = { selected ->
                    val newEnd = LocalDate.parse(selected, dateFormatter)
                    val currentStart = state.dateRange.first
                    onEvent(AllGamesEvent.DateRangeChanged(currentStart, newEnd))
                },
                label = "עד תאריך",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HourPickerDropdownModern(
                selectedHour = state.timeRange.first,
                onHourSelected = { newStart ->
                    onEvent(AllGamesEvent.TimeRangeChanged(newStart, state.timeRange.second))
                },
                label = "שעת התחלה",
                modifier = Modifier.weight(1f),
            )

            HourPickerDropdownModern(
                selectedHour = state.timeRange.second,
                onHourSelected = { newEnd ->
                    onEvent(AllGamesEvent.TimeRangeChanged(state.timeRange.first, newEnd))
                },
                label = "שעת סיום",
                modifier = Modifier.weight(1f),
            )
        }

        // שורת כפתורים: מיון ואיפוס
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterDropdownModern(
                label = "מיון לפי",
                options = GameFilterManager.SortOption.entries,
                selectedOption = state.sortOption,
                onOptionSelected = {
                    it?.let { selected -> onEvent(AllGamesEvent.SortOptionChanged(selected)) }
                },
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            )

            OutlinedButton(
                onClick = { onEvent(AllGamesEvent.ClearFilters) },
                modifier = Modifier.weight(1f)
            ) {
                Text("איפוס סינון")
            }
        }
    }
}
