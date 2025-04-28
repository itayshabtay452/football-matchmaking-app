// FieldHeaderSection.kt – שורת שם מגרש + מפה

package com.example.soccergamesfinder.ui.screens.field

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CropSquare
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldHeaderSection(field: Field) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = field.name ?: "מגרש ללא שם",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            FieldDetailRow(Icons.Outlined.Place, "כתובת: ${field.address ?: "לא ידועה"}")
            FieldDetailRow(Icons.Outlined.CropSquare, "גודל: ${field.size ?: "לא צויין"}")
            FieldDetailRow(Icons.Outlined.WbSunny, "תאורה: ${if (field.lighting) "יש" else "אין"}")
        }

        FieldMap(
            latitude = field.latitude ?: 0.0,
            longitude = field.longitude ?: 0.0,
            modifier = Modifier
                .weight(1f)
                .height(120.dp)
        )
    }
}

@Composable
fun FieldDetailRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}
