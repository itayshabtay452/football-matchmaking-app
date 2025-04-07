// FieldList.kt
package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldList(
    fields: List<Field>,
    onCreateGameClick: (Field) -> Unit,
    onViewGamesClick: (Field) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(fields) { field ->
            FieldCard(
                field = field,
                onCreateGameClick = { onCreateGameClick(field) },
                onViewGamesClick = { onViewGamesClick(field) }
            )
        }
    }
}