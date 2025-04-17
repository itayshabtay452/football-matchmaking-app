// HomeScreen.kt
package com.example.soccergamesfinder.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.soccergamesfinder.ui.components.FieldSection
import com.example.soccergamesfinder.ui.components.GameList
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

/**
 * HomeScreen displays a personalized dashboard for the user.
 * It includes:
 * - User greeting and profile image
 * - Navigation to profile and logout
 * - CTA to add new fields
 * - Preview of recommended fields and games
 */
@Composable
fun HomeScreen(
    navActions: HomeScreenNavActions,
    currentUserViewModel: CurrentUserViewModel = hiltViewModel(),
    fieldListViewModel: FieldListViewModel = hiltViewModel(),
    gameListViewModel: GameListViewModel = hiltViewModel(),
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()
) {
    val userState = currentUserViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value
    val gameState = gameListViewModel.state.collectAsState().value

    val isLoading = userState.isLoading || fieldState.isLoading || gameState.isLoading
    val error = userState.error ?: fieldState.error ?: gameState.error

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("שגיאה: $error", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    val currentUser = userState.user
    val fields = fieldState.fields
    val games = gameState.games

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            // ברכת שלום עם תמונת פרופיל
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "שלום, ${currentUser?.nickname ?: "אורח"}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                currentUser?.profileImageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "תמונת פרופיל",
                        modifier = Modifier.size(48.dp).padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // כפתורים
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = navActions.navigateToProfile, modifier = Modifier.weight(1f)) {
                    Text("👤 פרופיל")
                }
                OutlinedButton(onClick = navActions.navigateToLogin, modifier = Modifier.weight(1f)) {
                    Text("🚪 התנתקות")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // כפתור הוספת מגרש
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("מכיר מגרש שלא קיים אצלנו?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* TODO: מעבר למסך הוספת מגרש */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("➕ הוסף מגרש")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🏟️ מגרשים
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("המגרשים המומלצים בשבילך", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = navActions.navigateToAllFields) {
                    Text("הצג הכל")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FieldSection(
                fields = fields.take(5),
                onFieldClick = { field ->
                    navActions.navigateToField(field.id)
                },
                onCreateGame = { field, newGame ->
                    gameDetailsViewModel.createGameAndAttach(newGame) { success ->
                        if (!success) {
                            // אפשר להראות הודעת שגיאה או טוסט
                        }
                    }
                }
            )

        }

        // 🕹️ משחקים
        item {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("המשחקים המומלצים בשבילך", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = navActions.navigateToAllGames) {
                    Text("הצג הכל")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            GameList(
                games = games.take(5),
                currentUser = currentUser,
                fields = fields,
                onJoinClick = { game ->
                    gameDetailsViewModel.joinGame(game)
                },
                onLeaveClick = { game ->
                    gameDetailsViewModel.leaveGame(game)
                },
                onDeleteClick = { game ->
                    gameDetailsViewModel.deleteGame(game)
                },
                onCardClick = { game ->
                    navActions.navigateToGame(game.id)
                }
            )
        }
    }
}

