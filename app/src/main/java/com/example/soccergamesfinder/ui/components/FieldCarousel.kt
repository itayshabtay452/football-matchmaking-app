package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.ui.components.FieldCard

@Composable
fun FieldCarousel(
    fields: List<Field>,
    onFieldClick: (Field) -> Unit,
    onCreateGame: (Field) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(fields.take(5), key = { it.id }) { field ->
            FieldCard(
                field = field,
                onClick = { onFieldClick(field) },
                onCreateGameClick = { onCreateGame(field) }
            )
        }
    }
}