package com.example.soccergamesfinder.ui.components.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.data.Field

@Composable
fun GameInfoSection(game: Game, field: Field?, creator: User?, participants: List<User>)
{
    Text("🎮 משחק במגרש", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(8.dp))

    Text("📍 מגרש: ${field?.name ?: "שם לא זמין"}")
    Text("📌 כתובת: ${field?.address ?: "כתובת לא זמינה"}")
    Text("👤 יוצר המשחק: ${creator?.name ?: "לא ידוע"}")

    Spacer(modifier = Modifier.height(8.dp))

    Text("👥 משתתפים (${participants.size}/${game.maxPlayers}):")
    participants.forEach {
        Text("- ${it.nickname}")
    }
}