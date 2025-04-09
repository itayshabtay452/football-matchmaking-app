package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SortDropdown(
    modifier: Modifier = Modifier,
    title: String = "מיין לפי:",
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedButton(onClick = { expanded = true }) {
            Text(optionLabel(selectedOption))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
