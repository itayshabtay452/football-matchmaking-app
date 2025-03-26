package com.example.soccergamesfinder.ui.components.field

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldHeader(field: Field?) {
        Text(
            text = field?.name ?: "שם לא זמין",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "📌 כתובת: ${field?.address ?: "כתובת לא זמינה"}",
            style = MaterialTheme.typography.bodyLarge
        )
}