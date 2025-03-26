package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.game.GameActions
import com.example.soccergamesfinder.ui.components.game.GameChatSection
import com.example.soccergamesfinder.ui.components.game.GameInfoWithField
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.FieldDetailsViewModel
import com.example.soccergamesfinder.viewmodel.GameViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun GameScreen(gameId: String, userViewModel: UserViewModel, navigateBack: () -> Unit) {

    val gameViewModel: GameViewModel = hiltViewModel()
    val fieldDetailsViewModel: FieldDetailsViewModel = hiltViewModel()

    val game by gameViewModel.game.collectAsState()
    val uiState by fieldDetailsViewModel.uiState.collectAsState()
    val creator by gameViewModel.creator.collectAsState()
    val participants by gameViewModel.participants.collectAsState()
    val userId by userViewModel.userId.collectAsState()

    LaunchedEffect(gameId) {
        gameViewModel.getGame(gameId)
    }

    LaunchedEffect(game) {
        game?.let {
            fieldDetailsViewModel.loadField(it.fieldId)
            gameViewModel.loadGameUsers(it)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (game == null) {
            Text(text = "Loading...", style = MaterialTheme.typography.headlineMedium)
        } else {
            GameInfoWithField(
                game = game!!,
                uiState = uiState,
                creator = creator,
                participants = participants
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameActions(
                game = game!!,
                userId = userId!!,
                onJoin = {
                    gameViewModel.validateAndJoinGame(game!!, userId!!) {
                        if (it is ValidationResult.Success) {
                            navigateBack()
                        }
                    }
                },
                onLeave = {
                    gameViewModel.leaveGame(game!!, userId!!)
                },
                onDelete = {
                    gameViewModel.deleteGame(game!!, userId!!)
                    navigateBack()
                        }
            )
        }

        if (game != null && userId in game!!.players) {
            GameChatSection(gameId = gameId, userId = userId!!)
        }
    }
}

