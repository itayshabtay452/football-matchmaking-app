
package com.example.soccergamesfinder.ui.screens

import android.location.Location
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.viewmodel.FieldsViewModel

@Composable
fun FieldsListScreen(fieldsViewModel: FieldsViewModel, userLocation: Location?, navController: NavController) {
    val filteredFields by fieldsViewModel.filteredFields.collectAsState()
    val allFields by fieldsViewModel.allFields.collectAsState()

    val selectedSize by fieldsViewModel.selectedSize.collectAsState()
    val selectedType by fieldsViewModel.selectedType.collectAsState()
    val hasLighting by fieldsViewModel.hasLighting.collectAsState()
    val paid by fieldsViewModel.paid.collectAsState()

    LaunchedEffect(userLocation) {
        fieldsViewModel.loadFields(userLocation)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // כרטיס פילטרים
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenuField("גודל", allFields.map { it.fieldSize }.distinct(), selectedSize) {
                    fieldsViewModel.setFilter(it, selectedType, hasLighting, paid)
                }
                DropdownMenuField("סוג", allFields.map { it.fieldType }.distinct(), selectedType) {
                    fieldsViewModel.setFilter(selectedSize, it, hasLighting, paid)
                }
                ToggleChip("תאורה", hasLighting) { fieldsViewModel.setFilter(selectedSize, selectedType, !hasLighting, paid) }
                ToggleChip("חינם", paid) { fieldsViewModel.setFilter(selectedSize, selectedType, hasLighting, !paid) }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { fieldsViewModel.applyFilters() },
                modifier = Modifier.weight(1f)
            ) {
                Text("סנן", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { fieldsViewModel.clearFilters() },
                modifier = Modifier.weight(1f)
            ) {
                Text("נקה סינון", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredFields) { field ->
                AnimatedFieldCard(field, userLocation, navController)
            }
        }
    }
}



@Composable
fun DropdownMenuField(label: String, options: List<String>, selectedOption: String?, onOptionSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption ?: label) }

    Box(modifier = Modifier.width(90.dp)) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedText, fontSize = 12.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedText = option
                    onOptionSelected(option)
                    expanded = false
                }, text = { Text(option, fontSize = 12.sp) })
            }
            DropdownMenuItem(onClick = {
                selectedText = label
                onOptionSelected(null)
                expanded = false
            }, text = { Text("נקה בחירה", fontSize = 12.sp) })
        }
    }
}

@Composable
fun ToggleChip(label: String, isSelected: Boolean, onToggle: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onToggle,
        label = { Text(label, fontSize = 12.sp) },
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun AnimatedFieldCard(field: Field, userLocation: Location?, navController: NavController) {
    val fieldLocation = Location("").apply {
        latitude = field.latitude
        longitude = field.longitude
    }
    val distanceKm = userLocation?.distanceTo(fieldLocation)?.div(1000)?.let { "%.1f ק\"מ".format(it) } ?: "לא זמין"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
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