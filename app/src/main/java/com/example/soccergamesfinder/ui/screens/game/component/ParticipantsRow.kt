package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.model.User

@Composable
fun ParticipantsRow(
    creator: User?,
    participants: List<User>,
    onUserClick: (User) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("משתתפים", style = MaterialTheme.typography.titleMedium)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // הכרטיס הראשון – היוצר
            creator?.let {
                item {
                    ParticipantCard(
                        user = it,
                        isCreator = true,
                        onClick = { onUserClick(it) }
                    )
                }
            }

            // שאר המשתתפים (ללא היוצר)
            items(participants.filter { it.id != creator?.id }) { user ->
                ParticipantCard(user = user, onClick = { onUserClick(user) })
            }
        }
    }
}

