package com.example.soccergamesfinder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.FieldDetailsViewModel

@Composable
fun FieldDetailsScreen(
    navController: NavController,
    fieldName: String,
    viewModel: FieldDetailsViewModel = viewModel()
) {
    val fieldDetails by viewModel.fieldDetails.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFieldDetails(fieldName)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "פרטי מגרש", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        fieldDetails?.let { details ->
            Text(text = "שם המגרש: ${details.name}", style = MaterialTheme.typography.bodyLarge)
        } ?: CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("חזור")
        }
    }
}