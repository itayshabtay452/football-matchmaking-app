// FieldList.kt
package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel

@Composable
fun FieldList(
    fields: List<Field>,
    onViewGamesClick: (Field) -> Unit,
    fieldListViewModel: FieldListViewModel,
    gameListViewModel: GameListViewModel
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(fields) { field ->
            FieldCard(
                field = field,
                onViewGamesClick = { onViewGamesClick(field) },
                fieldListViewModel = fieldListViewModel,
                gameListViewModel = gameListViewModel
            )
        }
    }
}