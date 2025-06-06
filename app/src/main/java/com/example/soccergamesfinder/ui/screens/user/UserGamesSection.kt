package com.example.soccergamesfinder.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.ui.components.GameCard


@Composable
fun UserGamesSection(
    title: String,
    games: List<Game>,
    onGameClick: (Game) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game = game,
                    showJoinButton = false,
                    showLeaveButton = false,
                    showDeleteButton = false,
                    onCardClick = { onGameClick(game) }
                )
            }
        }
    }
}
