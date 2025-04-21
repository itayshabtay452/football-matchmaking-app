package com.example.soccergamesfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User

@Composable
fun GameSection(
    games: List<Game>,
    currentUser: User?,
    fields: List<Field>,
    onJoinClick: (Game) -> Unit,
    onLeaveClick: (Game) -> Unit,
    onDeleteClick: (Game) -> Unit,
    onCardClick: (Game) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                onCardClick  = { onCardClick(game) }
            )
        }
    }
}
