package com.example.soccergamesfinder.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.User


@Composable
fun UserProfileSection(user: User?) {

    if (user != null) {
        Text("👤 שם: ${user.name}", style = MaterialTheme.typography.bodyLarge)
        Text("📛 כינוי: ${user.nickname}", style = MaterialTheme.typography.bodyLarge)
        Text("🎂 גיל: ${user.age}", style = MaterialTheme.typography.bodyLarge)

        user.profileImageUrl?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "תמונת פרופיל",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        } ?: Text("❌ לא נבחרה תמונת פרופיל")
    } else {
        Text("🔄 טוען נתוני משתמש...")
    }
}