package com.example.soccergamesfinder.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.soccergamesfinder.ui.components.GameCard
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
            ErrorDialog(errorMessage) { openGamesViewModel.clearErrorMessage() }

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
fun ErrorDialog(errorMessage: String?, onDismiss: () -> Unit) {
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("אישור")
                }
            },
            title = { Text("שגיאה") },
            text = { Text(errorMessage) }
        )
    }
}
