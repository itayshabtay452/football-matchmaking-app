package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.FieldViewModel
import com.example.soccergamesfinder.viewmodel.GameViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun GameScreen(gameId: String, userViewModel: UserViewModel, navigateBack: () -> Unit) {

    val gameViewModel: GameViewModel = hiltViewModel()
    val fieldViewModel: FieldViewModel = hiltViewModel()

    val game by gameViewModel.game.collectAsState()
    val field by fieldViewModel.field.collectAsState()
    val creator by gameViewModel.creator.collectAsState()
    val participants by gameViewModel.participants.collectAsState()
    val userId by userViewModel.userId.collectAsState()

    LaunchedEffect(gameId) {
        gameViewModel.getGame(gameId)
    }

    LaunchedEffect(game) {
        game?.let {
            fieldViewModel.loadField(it.fieldId)
            gameViewModel.loadGameUsers(it)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (game == null || field == null) {
            Text(text = "Loading...", style = MaterialTheme.typography.headlineMedium)
        } else {
            Text("🎮 משחק במגרש", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📍 מגרש: ${field?.name ?: "שם לא זמין"}")
            Text("📌 כתובת: ${field?.address ?: "כתובת לא זמינה"}")
            Text("👤 יוצר המשחק: ${creator?.name ?: "לא ידוע"}")

            Text("👥 משתתפים (${participants.size}/${game!!.maxPlayers}):")
            participants.forEach {
                Text("- ${it.nickname}")
            }
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
        if (game != null && userId != null && userId in game!!.players) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "💬 צ'אט המשחק",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ChatScreen(gameId = gameId, userId = userId!!)
        }
    }
}

@Composable
fun GameActions(game: Game, userId: String, onJoin: () -> Unit, onLeave: () -> Unit,
                onDelete: () -> Unit) {

    when {
        userId == game.creatorId -> {
            Button(onClick = onDelete) {
                Text("Delete Game")
            }
        }
        userId in game.players -> {
            Button(onClick = onLeave) {
                Text("Leave Game")
            }
        }
        else -> {
            Button(onClick = onJoin, enabled = !game.isGameFull()) {
                Text("Join Game")
            }
        }
    }



}

