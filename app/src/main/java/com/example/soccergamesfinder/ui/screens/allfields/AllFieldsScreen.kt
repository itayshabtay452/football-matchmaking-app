// AllFieldsScreen.kt
package com.example.soccergamesfinder.ui.screens.allfields

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.FieldSection
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFieldsScreen(
    onViewGamesClick: (String) -> Unit,
    fieldListViewModel: FieldListViewModel = hiltViewModel(),
) {
    val allFieldsViewModel: AllFieldsViewModel = hiltViewModel()

    val state = allFieldsViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value

    LaunchedEffect(fieldState.fields) {
        if (fieldState.fields.isNotEmpty())
            allFieldsViewModel.setInitialFields(fieldState.fields)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("כל המגרשים") }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "סינון ומיון מתקדמים",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                AllFieldsFilterSection(
                                    state = state,
                                    onEvent = allFieldsViewModel::onEvent
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "נמצאו ${state.filteredFields.size} מגרשים מתאימים",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FieldSection(
                            fields = state.filteredFields,
                            onFieldClick = { field ->
                                onViewGamesClick(field.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

