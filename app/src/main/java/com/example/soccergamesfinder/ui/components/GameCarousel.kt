package com.example.soccergamesfinder.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.ui.components.GameCard

@Composable
fun GameCarousel(
    games: List<Game>,
    fields: List<Field>,
    currentUser: User?,
    onJoinClick: (Game) -> Unit,
    onLeaveClick: (Game) -> Unit,
    onDeleteClick: (Game) -> Unit,
    onCardClick: (Game) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(games, key = { it.id }) { game ->
            val isParticipant = currentUser?.id in game.joinedPlayers
            val isCreator = currentUser?.id == game.creatorId
            val field = fields.firstOrNull { it.id == game.fieldId }

            GameCard(
                game = game,
                field = field,
                showJoinButton = !isParticipant && game.joinedPlayers.size < game.maxPlayers,
                showLeaveButton = isParticipant && !isCreator,
                showDeleteButton = isCreator,
                showChatButton = isParticipant || isCreator,
                onJoinClick = { onJoinClick(game) },
                onLeaveClick = { onLeaveClick(game) },
                onDeleteClick = { onDeleteClick(game) },
                onCardClick = { onCardClick(game) }
            )
        }
    }
}
