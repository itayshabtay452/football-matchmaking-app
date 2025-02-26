package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownMenuField(label: String, options: List<String>, selectedOption: String?, onOptionSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption ?: label) }

    Box(modifier = Modifier.width(90.dp)) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedText, fontSize = 12.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedText = option
                    onOptionSelected(option)
                    expanded = false
                }, text = { Text(option, fontSize = 12.sp) })
            }
            DropdownMenuItem(onClick = {
                selectedText = label
                onOptionSelected(null)
                expanded = false
            }, text = { Text("נקה בחירה", fontSize = 12.sp) })
        }
    }
}

@Composable
fun ToggleChip(label: String, isSelected: Boolean, onToggle: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onToggle,
        label = { Text(label, fontSize = 12.sp) },
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}
