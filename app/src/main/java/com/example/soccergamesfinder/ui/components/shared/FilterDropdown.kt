package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun <T> FilterDropdown(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    optionToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        modifier = modifier
    ) {
        Text(text = selectedOption?.let(optionToString) ?: "בחר $label")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("ללא סינון") },
            onClick = {
                onOptionSelected(null)
                expanded = false
            }
        )

        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(optionToString(option)) },
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                }
            )
        }
    }
}
