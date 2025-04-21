// FilterComponents.kt – גרסה מודרנית לכל קומפוננטות הסינון

package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun HourPickerDropdownModern(
    selectedHour: Int?,
    onHourSelected: (Int?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val options = (8..23).toList()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(Icons.Outlined.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "$label: ${selectedHour?.let { String.format("%02d:00", it) } ?: "לא נבחר"}",
                style = MaterialTheme.typography.labelSmall
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("ללא סינון", style = MaterialTheme.typography.bodySmall) },
                onClick = {
                    onHourSelected(null)
                    expanded = false
                }
            )
            options.forEach { hour ->
                DropdownMenuItem(
                    text = { Text(String.format("%02d:00", hour), style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onHourSelected(hour)
                        expanded = false
                    }
                )
            }
        }
    }
}
