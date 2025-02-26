package com.example.soccergamesfinder.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.OpenGamesViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.GameWithFieldName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenGamesScreen(
    navController: NavController,
    openGamesViewModel: OpenGamesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(), // נוסיף את AuthViewModel
    userLocation: Location?
) {
    val openGames by openGamesViewModel.openGames.collectAsState()
    val errorMessage by openGamesViewModel.errorMessage.collectAsState() // נעקוב אחרי הודעות שגיאה
    val maxDistanceKm by openGamesViewModel.maxDistanceKm.collectAsState()
    val userId = authViewModel.user.value?.uid ?: "" // נקבל את ה-UID של המשתמש


    LaunchedEffect(userLocation) {
        openGamesViewModel.loadOpenGames(userLocation)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("משחקים פתוחים") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "חזור")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { openGamesViewModel.clearErrorMessage() }, // סגירת הדיאלוג
                    confirmButton = {
                        Button(onClick = { openGamesViewModel.clearErrorMessage() }) {
                            Text("אישור")
                        }
                    },
                    title = { Text("שגיאה") },
                    text = { Text(errorMessage ?: "") }
                )
            }
            Text("סינון לפי מרחק", style = MaterialTheme.typography.titleMedium)

            Slider(
                value = maxDistanceKm.toFloat(),
                onValueChange = { openGamesViewModel.setMaxDistance(it.toInt()) },
                valueRange = 1f..50f,
                steps = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Text("הצגת משחקים עד $maxDistanceKm ק\"מ", style = MaterialTheme.typography.bodyLarge)

            if (openGames.isEmpty()) {
                Text("אין משחקים פתוחים כרגע.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(openGames) { gameWithFieldName ->
                        GameCard(
                            gameWithFieldName = gameWithFieldName,
                            userId = userId,
                            onJoinClick = { gameId, gameDate ->
                                openGamesViewModel.joinGame(gameId, userId, gameDate)
                            },
                            onLeaveClick = { gameId ->
                                openGamesViewModel.leaveGame(gameId, userId) // קריאה לעזיבת המשחק
                            },
                            onDeleteClick = { gameId ->
                                openGamesViewModel.deleteGame(gameId) // קריאה למחיקת המשחק
                            }
                        )
                    }
                }




            }
        }
    }
}



@Composable
fun GameCard(
    gameWithFieldName: GameWithFieldName,
    userId: String,
    onJoinClick: (String, String) -> Unit,
    onLeaveClick: (String) -> Unit, // פונקציה לעזיבת משחק
    onDeleteClick: (String) -> Unit // פונקציה למחיקת משחק
) {
    val game = gameWithFieldName.game
    val fieldName = gameWithFieldName.fieldName
    val createdByUserName = gameWithFieldName.createdByUserName
    val distance = gameWithFieldName.distanceFromUser
    val isUserCreator = game.createdByUserId == userId // האם המשתמש הוא היוצר של המשחק
    val isUserInGame = game.players.contains(userId) // האם המשתמש כבר במשחק
    val playersCount = game.players.size
    val maxPlayers = game.maxPlayers
    val spotsLeft = maxPlayers - playersCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("מגרש: $fieldName", style = MaterialTheme.typography.titleLarge)
            Text("נפתח על ידי: $createdByUserName", style = MaterialTheme.typography.bodyMedium)
            Text("תאריך: ${game.date}", style = MaterialTheme.typography.bodyLarge)
            Text("שעות: ${game.timeRange}", style = MaterialTheme.typography.bodyLarge)
            Text("שחקנים: $playersCount/$maxPlayers", style = MaterialTheme.typography.bodyLarge)
            distance?.let {
                Text("מרחק: %.1f ק\"מ".format(it), style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ אם המשתמש יצר את המשחק → רק אפשרות למחוק
            if (isUserCreator) {
                Button(
                    onClick = { onDeleteClick(game.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("מחק משחק", color = Color.White)
                }
            }
            // ✅ אם המשתמש **לא יצר את המשחק**, יש 2 אפשרויות:
            else {
                // 🔹 אם המשתמש כבר במשחק → כפתור לעזיבה
                if (isUserInGame) {
                    Button(
                        onClick = { onLeaveClick(game.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("עזוב משחק", color = Color.White)
                    }
                }
                // 🔹 אם המשתמש לא במשחק ויש מקום → כפתור הצטרפות
                else if (spotsLeft > 0) {
                    Button(
                        onClick = { onJoinClick(game.id, game.date) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("הצטרף למשחק")
                    }
                } else {
                    // 🔹 אם המשחק מלא ואין מקום
                    Text("המשחק מלא!", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



