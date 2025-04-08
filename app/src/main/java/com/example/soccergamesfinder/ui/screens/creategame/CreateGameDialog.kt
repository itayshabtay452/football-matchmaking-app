// CreateGameDialog.kt
package com.example.soccergamesfinder.ui.screens.creategame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.ui.screens.creategame.components.DatePickerField
import com.example.soccergamesfinder.ui.screens.creategame.components.EndTimePickerDropdown
import com.example.soccergamesfinder.ui.screens.creategame.components.MaxPlayersDropdown
import com.example.soccergamesfinder.ui.screens.creategame.components.TimePickerDropdown
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel

@Composable
fun CreateGameDialog(
    field: Field,
    onDismiss: () -> Unit
) {
    val viewModel: CreateGameViewModel = hiltViewModel()
    val gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()
    val gameListViewModel: GameListViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState().value

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onEvent(CreateGameEvent.Submit, field) { game ->
                        gameDetailsViewModel.createGame(game) { success ->
                            if (success) {
                                gameListViewModel.addGame(game)
                                onDismiss()
                            }
                        }
                    }
                },
                enabled = !state.isLoading
            ) {
                Text("צור משחק")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = {
                viewModel.onEvent(CreateGameEvent.Cancel, field) {}
                onDismiss()
            }) {
                Text("ביטול")
            }
        },
        title = { Text("צור משחק חדש") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // תאריך
                DatePickerField(
                    selectedDate = state.date,
                    onDateSelected = { viewModel.onEvent(CreateGameEvent.DateSelected(it), field) {} }
                )

                TimePickerDropdown(
                    selectedTime = state.startTime,
                    onTimeSelected = { viewModel.onEvent(CreateGameEvent.StartTimeSelected(it), field) {} },
                    label = "שעת התחלה"
                )

                EndTimePickerDropdown(
                    startTime = state.startTime,
                    selectedEndTime = state.endTime,
                    onEndTimeSelected = { viewModel.onEvent(CreateGameEvent.EndTimeSelected(it), field) {} },
                    label = "שעת סיום"
                )


                // מספר שחקנים
                MaxPlayersDropdown(
                    selected = state.maxPlayers,
                    onSelected = { viewModel.onEvent(CreateGameEvent.MaxPlayersChanged(it), field) {} }
                )

                // תיאור
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onEvent(CreateGameEvent.DescriptionChanged(it), field) {} },
                    label = { Text("תיאור המשחק (אופציונלי)") },
                    enabled = !state.isLoading
                )

                // שגיאה
                state.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}
