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
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.GameViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun GameScreen(gameId: String, userViewModel: UserViewModel, navigateBack: () -> Unit) {

    val gameViewModel: GameViewModel = hiltViewModel()

    val game by gameViewModel.game.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val errorMessage by gameViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.getGame(gameId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (game == null) {
            Text("⏳ טוען נתוני משחק...", style = MaterialTheme.typography.headlineMedium)
        } else {
            Text("🎮 משחק", style = MaterialTheme.typography.headlineMedium)
            Text("📍 מגרש: ${game!!.fieldId}")
            Text("🕒 שעת התחלה: ${game!!.getFormattedStartTime()}")
            Text("🏁 שעת סיום: ${game!!.getFormattedEndTime()}")
            Text("👤 יוצר המשחק: ${game!!.creatorId}")
            Text("👥 משתתפים: ${game!!.players.size}/${game!!.maxPlayers}")
            Text("📜 תיאור: ${game!!.description ?: "אין תיאור"}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userId?.let {
                        gameViewModel.validateAndJoinGame(game!!, it) { result ->
                            if (result is ValidationResult.Success) {
                                navigateBack()
                            }
                        }
                    }
                },
                enabled = game?.isGameFull() == false && game?.players?.contains(userId) == false
            ) {
                Text("🚀 הצטרף למשחק")
            }

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (game!!.creatorId == userId) {
                Button(onClick = {
                    gameViewModel.deleteGame(game!!, userId!!)
                    navigateBack()}) {
                    Text("🗑️ מחק משחק")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (userId in game!!.players && userId != game!!.creatorId) {
                Button(onClick = {
                    userId?.let { gameViewModel.leaveGame(game!!, it) } }) {
                    Text("🚪 עזוב משחק")
                }
            }


        }

    }
}

