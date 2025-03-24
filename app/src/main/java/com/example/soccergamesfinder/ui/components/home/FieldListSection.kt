package com.example.soccergamesfinder.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field


fun LazyListScope.FieldListSection(isLoading: Boolean, fields: List<Field>, onLoadMore: () -> Unit,
                     onFieldClick: (String) -> Unit) {
    if (isLoading) {
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
        items(fields) {field ->
            FieldItem(field = field, onClick = onFieldClick)
        }
        item {
            Button(onClick = onLoadMore, modifier = Modifier.fillMaxWidth().padding(8.dp))
            { Text("🔄 טען עוד") }
        }
    }
}