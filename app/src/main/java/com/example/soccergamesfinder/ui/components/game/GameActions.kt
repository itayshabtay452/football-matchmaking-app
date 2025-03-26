package com.example.soccergamesfinder.ui.components.game

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soccergamesfinder.data.Game

@Composable
fun GameActions(
    game: Game,
    userId: String,
    onJoin: () -> Unit,
    onLeave: () -> Unit,
    onDelete: () -> Unit
) {
    when {
        userId == game.creatorId -> {
            Button(onClick = onDelete) {
                Text("🗑️ מחק משחק")
            }
        }
        userId in game.players -> {
            Button(onClick = onLeave) {
                Text("🚪 עזוב משחק")
            }
        }
        else -> {
            Button(onClick = onJoin, enabled = !game.isGameFull()) {
                Text("🚀 הצטרף למשחק")
            }
        }
    }
}
