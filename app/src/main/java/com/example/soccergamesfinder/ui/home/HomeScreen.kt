package com.example.soccergamesfinder.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.soccergamesfinder.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// דגם נתונים פשוט למשחק
data class Game(
    val id: String,
    val title: String,
    val date: String
)

// רשימת משחקים לדוגמה
val sampleGames = listOf(
    Game("1", "משחק ראשון", "12:00, 20/02"),
    Game("2", "משחק שני", "15:00, 21/02"),
    Game("3", "משחק שלישי", "18:00, 22/02")
)

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onCreateGame: () -> Unit,
    onGameSelected: (gameId: String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // תמונת רקע מלאה
        Image(
            painter = painterResource(id = R.drawable.login), // ודא שהתמונה קיימת ב-res/drawable
            contentDescription = "Home Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient overlay: מתחיל שקוף ומסתיים בשחור כדי להדגיש את האלמנטים
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )
        // תוכן המסך
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // חבילת עליונה עם התנתקות וברכת שלום
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "שלום, משתמש", // כאן ניתן להחליף בשם המשתמש
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
                )
                IconButton(onClick = onSignOut) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "התנתק",
                        tint = Color.White
                    )
                }
            }
            // תוכן מרכזי: ברכת "משחקים מומלצים" ורשימה אופקית
            Column {
                Text(
                    text = "משחקים מומלצים",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    items(sampleGames) { game ->
                        GameCard(game = game, onClick = { onGameSelected(game.id) })
                    }
                }
            }
            // כפתור למעבר למסך יצירת משחק חדש (Floating Action Button בגרסה פשוטה)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onCreateGame,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("צור משחק חדש", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.login), // ודא שיש לך תמונה placeholder
                contentDescription = "Game Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = game.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = game.date, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
