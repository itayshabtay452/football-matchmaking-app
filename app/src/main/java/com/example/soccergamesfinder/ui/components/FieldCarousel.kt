package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.model.Field

@Composable
fun FieldCarousel(
    fields: List<Field>,
    followedFields: List<String>,
    onFieldClick: (Field) -> Unit,
    onCreateGame: (Field) -> Unit,
    onFollowFieldClick: (Field) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(fields, key = { it.id }) { field ->
            FieldCard(
                field = field,
                isFollowed = followedFields.contains(field.id),
                onFollowClick = { onFollowFieldClick(field) },
                onClick = { onFieldClick(field) },
                onCreateGameClick = { onCreateGame(field) }
            )
        }
    }
}
