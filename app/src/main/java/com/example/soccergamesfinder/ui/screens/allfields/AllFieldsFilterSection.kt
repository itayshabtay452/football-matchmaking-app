// AllFieldsFilterSection.kt – גרסה מודרנית עם קומפוננטות משודרגות

package com.example.soccergamesfinder.ui.screens.allfields

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.WbSunny
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // 🔍 שם מגרש
            FilterTextFieldModern(
                label = "שם מגרש",
                value = state.nameQuery,
                onValueChange = { onEvent(AllFieldsEvent.NameChanged(it)) },
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.weight(1f)
            )

            // 🏙️ עיר
            FilterTextFieldModern(
                label = "עיר",
                value = state.cityQuery,
                onValueChange = { onEvent(AllFieldsEvent.CityChanged(it)) },
                leadingIcon = {
                    Icon(Icons.Default.LocationCity, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.weight(1f)
            )

        }

        // 📏 גודל מגרש
        FilterDropdownModern(
            label = "גודל מגרש",
            options = listOf("קטן", "בינוני", "גדול"),
            selectedOption = state.selectedSize,
            onOptionSelected = { onEvent(AllFieldsEvent.SizeChanged(it)) },
            leadingIcon = {
                Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        )

        // 💡 תאורה
        FilterSwitchModern(
            label = "רק מגרשים עם תאורה",
            checked = state.lightingOnly,
            onCheckedChange = { onEvent(AllFieldsEvent.LightingChanged(it)) },
            leadingIcon = Icons.Outlined.WbSunny
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // 📍 מרחק מקסימלי
            FilterSliderModern(
                label = "מרחק מקסימלי (בק\"מ)",
                value = state.maxDistance?.toFloat(),
                valueRange = 1f..30f,
                steps = 0,
                onValueChange = { onEvent(AllFieldsEvent.MaxDistanceChanged(it?.toDouble())) },
                leadingIcon = Icons.Default.Place,
                modifier = Modifier.weight(1f)
            )

            // 🎮 מינימום משחקים
            FilterSliderModern(
                label = "כמות משחקים מינימלית",
                value = state.minGameCount?.toFloat(),
                valueRange = 1f..30f,
                steps = 29,
                onValueChange = { onEvent(AllFieldsEvent.MinGameCountChanged(it?.toInt())) },
                leadingIcon = Icons.Outlined.SportsSoccer,
                modifier = Modifier.weight(1f)
            )
        }

        // 🔽 מיון + איפוס
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterDropdownModern(
                label = "מיון לפי",
                options = FieldFilterManager.SortOption.entries.map { it.name },
                selectedOption = state.sortOption,
                onOptionSelected = { selected ->
                    FieldFilterManager.SortOption.entries.find { it.name == selected }
                        ?.let { onEvent(AllFieldsEvent.SortOptionChanged(it)) }
                },
                leadingIcon = {
                    Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = { onEvent(AllFieldsEvent.ClearFilters) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("איפוס", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
