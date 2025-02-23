package com.example.soccergamesfinder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.data.FieldModel
import com.example.soccergamesfinder.viewmodel.FieldsViewModel
import com.example.soccergamesfinder.ui.utils.LocationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldsScreen(
    navController: NavController,
    userLatitude: Double,
    userLongitude: Double,
    fieldsViewModel: FieldsViewModel = viewModel()
) {
    val fields by fieldsViewModel.fields.collectAsState()

    LaunchedEffect(userLatitude, userLongitude) {
        fieldsViewModel.loadFields(userLatitude, userLongitude)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("מגרשי כדורגל קרובים אליך") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(fields) { field ->
                FieldItem(field, userLatitude, userLongitude)
            }
        }
    }
}

@Composable
fun FieldItem(field: FieldModel, userLat: Double, userLng: Double) {
    val distance = remember(field) {
        LocationUtils.calculateDistance(
            userLat, userLng, field.latitude, field.longitude
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = field.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "סוג מגרש: ${field.fieldType}")
            Text(text = "גודל: ${field.fieldSize}")
            Text(text = "תאורה: ${if (field.hasLighting) "יש" else "אין"}")
            Text(text = "בתשלום: ${if (field.isPaid) "כן" else "לא"}")
            Text(text = "דורש אישור: ${if (field.requiresOwnerApproval) "כן" else "לא"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "מרחק ממך: ${"%.2f".format(distance)} ק\"מ",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
