package com.example.soccergamesfinder.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field


fun LazyListScope.FieldListSection(
    isLoading: Boolean,
    fields: List<Field>,
    onLoadMore: () -> Unit,
    onFieldClick: (String) -> Unit
) {
    if (isLoading && fields.isEmpty()) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    } else if (fields.isEmpty()) {
        item {
            Text("❌ אין מתקנים זמינים", color = MaterialTheme.colorScheme.error)
        }
    } else {
        itemsIndexed(fields) { index, field ->
            FieldItem(field = field, onClick = { onFieldClick(field.id) })

            if (index == fields.lastIndex) {
                // טען עוד מגרשים בסוף הרשימה
                LaunchedEffect(key1 = index) {
                    onLoadMore()
                }
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
