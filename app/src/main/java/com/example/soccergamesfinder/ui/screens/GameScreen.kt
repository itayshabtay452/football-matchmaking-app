package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Column
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
import com.example.soccergamesfinder.viewmodel.GameViewModel
import kotlinx.coroutines.delay


@Composable
fun GameScreen(gameId: String) {

    val gameViewModel: GameViewModel = hiltViewModel()

    val game by gameViewModel.game.collectAsState()

    LaunchedEffect(Unit) {
        delay(100) // עיכוב קל להבטחת שהמסך הישן ייצא לחלוטין
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
        }
    }
}

