package com.example.soccergamesfinder.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldItem(field: Field, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🏟️ ${field.name ?: "לא ידוע"}", style = MaterialTheme.typography.titleMedium)
                Text("📍 ${field.address ?: "לא ידוע"}")
                Text("📐 גודל: ${field.size ?: "לא ידוע"}")
                Text("💡 תאורה: ${field.lighting ?: "לא ידוע"}")


                field.distance?.let {
                    Text("📏 מרחק: ${"%.2f".format(it)} ק\"מ")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { onClick(field.id) }) {
                    Text("🔍 צפה בפרטים")
                }
            }
        }
}