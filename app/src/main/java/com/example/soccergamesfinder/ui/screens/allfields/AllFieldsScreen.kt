// AllFieldsScreen.kt
package com.example.soccergamesfinder.ui.screens.allfields

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.FieldCarousel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFieldsScreen(
    onViewGamesClick: (String) -> Unit,
    fieldListViewModel: FieldListViewModel,
) {
    val allFieldsViewModel: AllFieldsViewModel = hiltViewModel()

    val state = allFieldsViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value

    LaunchedEffect(fieldState.fields) {
        if (fieldState.fields.isNotEmpty())
            allFieldsViewModel.setInitialFields(fieldState.fields)
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    AllFieldsFilterSection(
                        state = state,
                        onEvent = allFieldsViewModel::onEvent
                    )
                }

                Text(
                    text = "🎯 נמצאו ${state.filteredFields.size} מגרשים מתאימים",
                    style = MaterialTheme.typography.labelSmall
                )
                FieldCarousel(
                    fields = state.filteredFields,
                    followedFields = fieldState.followedFields,
                    onFollowFieldClick = { fieldListViewModel.toggleFollowField(it.id) },
                    onFieldClick = { field -> onViewGamesClick(field.id) },
                    onCreateGame = { field ->
                    }
                )
            }
        }
    }
}

