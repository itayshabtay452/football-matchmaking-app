package com.example.soccergamesfinder.ui.screens.allfields

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.shared.*
import com.example.soccergamesfinder.utils.FieldFilterManager

@Composable
fun AllFieldsFilterSection(
    state: AllFieldsState,
    onEvent: (AllFieldsEvent) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // סינון לפי שם מגרש
        FilterTextField(
            label = "שם מגרש",
            value = state.nameQuery,
            onValueChange = { onEvent(AllFieldsEvent.NameChanged(it)) }
        )

        // סינון לפי עיר
        FilterTextField(
            label = "עיר",
            value = state.cityQuery,
            onValueChange = { onEvent(AllFieldsEvent.CityChanged(it)) }
        )

        // סינון לפי גודל מגרש
        FilterDropdown(
            label = "גודל מגרש",
            options = listOf("קטן", "בינוני", "גדול"),
            selectedOption = state.selectedSize,
            onOptionSelected = { onEvent(AllFieldsEvent.SizeChanged(it)) }
        )

        // סינון לפי תאורה
        FilterSwitch(
            label = "רק מגרשים עם תאורה",
            checked = state.lightingOnly,
            onCheckedChange = { onEvent(AllFieldsEvent.LightingChanged(it)) }
        )

        // סינון לפי מרחק
        FilterSlider(
            label = "מרחק מקסימלי (בק\"מ)",
            value = state.maxDistance?.toFloat(),
            valueRange = 1f..30f,
            steps = 0,
            onValueChange = { onEvent(AllFieldsEvent.MaxDistanceChanged(it?.toDouble())) }
        )

        // סינון לפי כמות משחקים מינימלית
        FilterSlider(
            label = "כמות משחקים מינימלית",
            value = state.minGameCount?.toFloat(),
            valueRange = 1f..30f,
            steps = 29,
            onValueChange = { onEvent(AllFieldsEvent.MinGameCountChanged(it?.toInt())) }
        )

        // מיון
        FilterDropdown(
            label = "מיון לפי",
            options = FieldFilterManager.SortOption.entries.map { it.name },
            selectedOption = state.sortOption,
            onOptionSelected = { selected ->
                FieldFilterManager.SortOption.entries.find { it.name == selected }
                    ?.let { onEvent(AllFieldsEvent.SortOptionChanged(it)) }
            }
        )

        // כפתור איפוס
        OutlinedButton(
            onClick = { onEvent(AllFieldsEvent.ClearFilters) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("איפוס סינון")
        }
    }
}
