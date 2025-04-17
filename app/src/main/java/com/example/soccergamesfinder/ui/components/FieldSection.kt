package com.example.soccergamesfinder.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.ui.screens.creategame.CreateGameDialog

@Composable
fun FieldSection(
    fields: List<Field>,
    onFieldClick: ((Field) -> Unit)? = null,
    onCreateGame: ((Field, Game) -> Unit)? = null, // 👈 חדש
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var selectedField by remember { mutableStateOf<Field?>(null) }

    Column(modifier = modifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(fields) { field ->
                FieldCard(
                    field = field,
                    onClick = { onFieldClick?.invoke(field) },
                    onCreateGameClick = { selectedField = field }
                )
            }
        }

        selectedField?.let { field ->
            CreateGameDialog(
                field = field,
                onDismiss = { selectedField = null },
                onCreateSuccess = { newGame ->
                    onCreateGame?.invoke(field, newGame)
                    selectedField = null
                }
            )
        }
    }
}