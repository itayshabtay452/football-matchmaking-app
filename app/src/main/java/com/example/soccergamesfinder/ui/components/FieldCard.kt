package com.example.soccergamesfinder.ui.components

import android.location.Location
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.soccergamesfinder.data.Field

@Composable
fun FieldCard(field: Field, userLocation: Location?, navController: NavController) {
    val fieldLocation = Location("").apply {
        latitude = field.latitude
        longitude = field.longitude
    }
    val distanceKm = userLocation?.distanceTo(fieldLocation)?.div(1000)?.let { "%.1f ק\"מ".format(it) } ?: "לא זמין"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(field.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("סוג: ${field.fieldType}", fontSize = 14.sp)
            Text("גודל: ${field.fieldSize}", fontSize = 14.sp)
            Text("תאורה: ${if (field.hasLighting) "כן" else "לא"}", fontSize = 14.sp)
            Text("בתשלום: ${if (field.paid) "כן" else "לא"}", fontSize = 14.sp)
            Text("מרחק: $distanceKm", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("createGameScreen/${field.id}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("צור משחק")
            }
        }
    }
}
