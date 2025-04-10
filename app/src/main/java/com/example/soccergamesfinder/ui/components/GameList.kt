package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User

@Composable
fun GameList(
    games: List<Game>,
    currentUser: User?,
    fields: List<Field>,
    onJoinClick: (Game) -> Unit,
    onLeaveClick: (Game) -> Unit,
    onDeleteClick: (Game) -> Unit,
    onCardClick: (Game) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        games.forEach { game ->
            val isParticipant = currentUser?.id in game.joinedPlayers
            val isCreator = currentUser?.id == game.creatorId
            val isFull = game.joinedPlayers.size >= game.maxPlayers

            val field = fields.firstOrNull { it.id == game.fieldId }

            GameCard(
                game = game,
                field = field,
                showJoinButton = !isParticipant && !isFull,
                showLeaveButton = isParticipant && !isCreator,
                showDeleteButton = isCreator,
                showChatButton = isParticipant || isCreator,
                onJoinClick = { onJoinClick(game) },
                onLeaveClick = { onLeaveClick(game) },
                onDeleteClick = { onDeleteClick(game) },
                onCardClick  = {  }
            )
        }
    }
}
