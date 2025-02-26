
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
import com.example.soccergamesfinder.ui.components.FieldCard
import com.example.soccergamesfinder.ui.components.DropdownMenuField
import com.example.soccergamesfinder.ui.components.ToggleChip
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
                FieldCard(field, userLocation, navController)
            }
        }
    }
}
