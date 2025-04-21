package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.shared.*
import com.example.soccergamesfinder.utils.GameFilterManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllGamesFilterCard(
    state: AllGamesState,
    onEvent: (AllGamesEvent) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("\uD83D\uDD0D מצא את המשחק שמתאים לך", style = MaterialTheme.typography.titleMedium)

            FilterTextField(
                label = "שם מגרש",
                value = state.fieldNameQuery,
                onValueChange = { onEvent(AllGamesEvent.FieldNameChanged(it)) }
            )

            FilterDropdown(
                label = "מצב משחק",
                options = listOf(null) + GameStatusOption.entries,
                selectedOption = state.selectedStatus,
                onOptionSelected = { onEvent(AllGamesEvent.StatusChanged(it)) },
                optionToString = { it?.name ?: "הכל" }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DatePickerField(
                    selectedDate = state.dateRange.first?.format(dateFormatter) ?: "לא נבחר",
                    onDateSelected = {
                        val newStart = LocalDate.parse(it, dateFormatter)
                        onEvent(AllGamesEvent.DateRangeChanged(newStart, state.dateRange.second))
                    },
                    label = "מתאריך",
                )
                DatePickerField(
                    selectedDate = state.dateRange.second?.format(dateFormatter) ?: "לא נבחר",
                    onDateSelected = {
                        val newEnd = LocalDate.parse(it, dateFormatter)
                        onEvent(AllGamesEvent.DateRangeChanged(state.dateRange.first, newEnd))
                    },
                    label = "עד תאריך",
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HourPickerDropdown(
                    selectedHour = state.timeRange.first,
                    onHourSelected = {
                        onEvent(AllGamesEvent.TimeRangeChanged(it, state.timeRange.second))
                    },
                    label = "שעת התחלה",
                    modifier = Modifier.weight(1f)
                )
                HourPickerDropdown(
                    selectedHour = state.timeRange.second,
                    onHourSelected = {
                        onEvent(AllGamesEvent.TimeRangeChanged(state.timeRange.first, it))
                    },
                    label = "שעת סיום",
                    modifier = Modifier.weight(1f)
                )
            }

            FilterDropdown(
                label = "מיון לפי",
                options = GameFilterManager.SortOption.entries,
                selectedOption = state.sortOption,
                onOptionSelected = {
                    if (it != null) onEvent(AllGamesEvent.SortOptionChanged(it))
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onEvent(AllGamesEvent.UpdateGames(state.filteredGames)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("החל סינון")
                }

                OutlinedButton(
                    onClick = { onEvent(AllGamesEvent.ClearFilters) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("איפוס")
                }
            }
        }
    }
}
