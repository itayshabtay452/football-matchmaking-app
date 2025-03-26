package com.example.soccergamesfinder.ui.components.creategame

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TitleSection(fieldName: String?) {
    Text("🆕 יצירת משחק חדש", style = MaterialTheme.typography.headlineMedium)
    Text("📍 מגרש: ${fieldName ?: "שם לא זמין"}", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(16.dp))
}
