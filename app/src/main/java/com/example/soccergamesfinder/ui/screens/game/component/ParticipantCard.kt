package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.User

@Composable
fun ParticipantCard(
    user: User,
    isCreator: Boolean = false,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .width(140.dp) // אפשר לשנות לפי הצורך
            .wrapContentHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // תמונה רחבה בחלק העליון של הכרטיס
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = "תמונת פרופיל",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // יחס קבוע לריבוע
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop // מילוי אחיד
            )

            // פרטים מתחת לתמונה
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(user.nickname ?: "ללא שם", style = MaterialTheme.typography.labelSmall)
                if (isCreator) {
                    Text(
                        "יוצר המשחק",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
