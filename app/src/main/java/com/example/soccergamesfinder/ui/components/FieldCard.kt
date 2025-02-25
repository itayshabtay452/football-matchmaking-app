package com.example.soccergamesfinder.ui.components

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import java.util.Locale

@Composable
fun FieldCard(field: Field) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = field.name, style = MaterialTheme.typography.titleLarge)
            Text("סוג: ${field.fieldType}")
            Text("גודל: ${field.fieldSize}")
            Text("תאורה: ${if (field.hasLighting) "יש" else "אין"}")
            Text("בתשלום: ${if (field.paid) "כן" else "לא"}")

            field.distanceFromUser?.let { distance ->
                Text("📍 מרחק: %.1f ק\"מ".format(distance), style = MaterialTheme.typography.bodyLarge)
            } ?: Text("📍 מרחק: לא ידוע")
        }
    }
}

