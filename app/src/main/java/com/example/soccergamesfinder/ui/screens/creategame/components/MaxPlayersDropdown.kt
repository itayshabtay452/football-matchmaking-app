// MaxPlayersDropdown.kt
package com.example.soccergamesfinder.ui.screens.creategame.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MaxPlayersDropdown(
    selected: Int,
    onSelected: (Int) -> Unit,
    label: String = "מספר שחקנים מקסימלי",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val options = (10..33).toList()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selected")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { number ->
                DropdownMenuItem(
                    text = { Text("$number שחקנים") },
                    onClick = {
                        onSelected(number)
                        expanded = false
                    }
                )
            }
        }
    }
}
