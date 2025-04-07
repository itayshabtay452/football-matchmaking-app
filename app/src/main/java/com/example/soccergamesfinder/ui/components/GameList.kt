package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User

@Composable
fun GameList(
    games: List<Game>,
    currentUser: User?,
    onJoinClick: (Game) -> Unit,
    onLeaveClick: (Game) -> Unit,
    onDeleteClick: (Game) -> Unit,
    onChatClick: (Game) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(games) { game ->
            GameCard(
                game = game,
                currentUser = currentUser,
                onJoinClick = { onJoinClick(game) },
                onLeaveClick = { onLeaveClick(game) },
                onDeleteClick = { onDeleteClick(game) },
                onChatClick = { onChatClick(game) }
            )
        }
    }
}