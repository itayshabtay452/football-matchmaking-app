package com.example.soccergamesfinder.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.ui.components.creategame.CreateGameButton
import com.example.soccergamesfinder.ui.components.field.FieldHeaderSection
import com.example.soccergamesfinder.ui.components.game.GameDetailsForm
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.FieldDetailsViewModel
import com.example.soccergamesfinder.viewmodel.GameViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel
import com.example.soccergamesfinder.viewmodel.GameValidationViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun CreateGameScreen(fieldId: String, userViewModel: UserViewModel, navigateBack: () -> Unit){

    val gameViewModel: GameViewModel = hiltViewModel()
    val fieldDetailsViewModel: FieldDetailsViewModel = hiltViewModel()
    val gameValidationViewModel: GameValidationViewModel = hiltViewModel()

    val uiState by fieldDetailsViewModel.uiState.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val startTimeValidation by gameValidationViewModel.startTimeValidation.collectAsState()
    val endTimeValidation  by gameValidationViewModel.endTimeValidation.collectAsState()
    val validStartTime by gameValidationViewModel.validStartTime.collectAsState()
    val validEndTime by gameValidationViewModel.validEndTime.collectAsState()
    val errorMessage by gameViewModel.errorMessage.collectAsState()

    var selectedDate by remember { mutableStateOf("") }
    var selectedStartTime by remember { mutableStateOf("") }
    var selectedEndTime by remember { mutableStateOf("10") }
    var maxPlayers by remember { mutableStateOf("10") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(fieldId) {
        fieldDetailsViewModel.loadField(fieldId)
    }

    Column(modifier = Modifier.padding(16.dp)) {

        FieldHeaderSection(uiState)

        GameDetailsForm(
            selectedDate = selectedDate,
            selectedStartTime = selectedStartTime,
            selectedEndTime = selectedEndTime,
            maxPlayers = maxPlayers,
            description = description,
            startTimeValidation = startTimeValidation,
            endTimeValidation = endTimeValidation,
            onDateChange = { selectedDate = it },
            onStartTimeChange = {
                selectedStartTime = it
                gameValidationViewModel.validateStartTime(selectedDate, it)
            },
            onEndTimeChange = {
                selectedEndTime = it
                gameValidationViewModel.validateEndTime(selectedDate, selectedStartTime, it)
            },
            onMaxPlayersChange = { maxPlayers = it },
            onDescriptionChange = { description = it }
        )

        CreateGameButton(
            enabled = validStartTime != null && validEndTime != null,
            errorMessage = errorMessage,
            onClick = {
                val game = Game(
                    fieldId = fieldId,
                    startTime = validStartTime!!,
                    endTime = validEndTime!!,
                    creatorId = userId!!,
                    maxPlayers = maxPlayers.toIntOrNull() ?: 10,
                    description = if (description.isNotBlank()) description else null
                )
                gameViewModel.validateAndCreateGame(game, userId!!) { result ->
                    if (result is ValidationResult.Success) {
                        navigateBack()
                    }
                }
            }
        )
    }
}

