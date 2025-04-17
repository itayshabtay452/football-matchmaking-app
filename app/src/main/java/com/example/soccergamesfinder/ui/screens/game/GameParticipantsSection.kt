// GameParticipantsSection.kt
package com.example.soccergamesfinder.ui.screens.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.User

@Composable
fun GameParticipantsSection(
    creator: User?,
    participants: List<User>,
    onUserClick: (User) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("משתתפים", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // היוצר
        creator?.let {
            UserCard(user = it, label = "יוצר המשחק", onClick = { onUserClick(it) })
            Spacer(modifier = Modifier.height(8.dp))
        }

        // שאר המשתתפים
        val others = participants.filter { it.id != creator?.id }
        if (others.isNotEmpty()) {
            Text("שחקנים נוספים:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            others.forEach { user ->
                UserCard(user = user, onClick = { onUserClick(user) })
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun UserCard(user: User, label: String? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = "תמונת פרופיל",
                modifier = Modifier.size(40.dp)
            )

            Column {
                Text(user.nickname ?: "ללא שם", style = MaterialTheme.typography.bodyLarge)
                if (!label.isNullOrBlank()) {
                    Text(label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
