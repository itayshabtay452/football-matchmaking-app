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
    homeViewModel: HomeViewModel = hiltViewModel(),
    currentUserViewModel: CurrentUserViewModel = hiltViewModel(),
    fieldListViewModel: FieldListViewModel = hiltViewModel(),
    gameListViewModel: GameListViewModel = hiltViewModel(),
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()
) {
    val userState = currentUserViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value
    val gameState = gameListViewModel.state.collectAsState().value

    LaunchedEffect(userState.user, fieldState.fields, gameState.games) {
        homeViewModel.updateState(
            userNickname = userState.user?.nickname ?: "",
            userProfileImageUrl = userState.user?.profileImageUrl,
            fields = fieldState.fields,
            games = gameState.games,
            isLoading = userState.isLoading || fieldState.isLoading || gameState.isLoading,
            error = userState.error ?: fieldState.error ?: gameState.error
        )
        println(">>> HomeScreen returned ${fieldState.fields.size} fields and ${gameState.games.size} games")
    }


    val state = homeViewModel.state.collectAsState().value

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("שגיאה: ${state.error}", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            // Greeting row with profile image
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "שלום, ${state.userNickname}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                state.userProfileImageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "תמונת פרופיל",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile and logout buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = navActions.navigateToProfile, modifier = Modifier.weight(1f)) {
                    Text("👤 פרופיל")
                }
                OutlinedButton(onClick = navActions.navigateToLogin, modifier = Modifier.weight(1f)) {
                    Text("🚪 התנתקות")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Add new field CTA card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("מכיר מגרש שלא קיים אצלנו?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* TODO: Navigate to add field screen */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("➕ הוסף מגרש")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Recommended Fields Section
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
                fields = state.fields.take(5),
                onCreateGame = { field, newGame ->
                    // כאן תוכל לעדכן את GameListViewModel ו־FieldListViewModel לפי הצורך
                    gameListViewModel.addGame(newGame)
                    fieldListViewModel.updateFieldWithNewGame(field.id, newGame.id) // תעדכן עם המזהה הנכון
                }
            )
        }

        // Recommended Games Section
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
                games = state.games.take(5),
                currentUser = userState.user,
                fields = fieldState.fields,
                onJoinClick = { game ->
                    gameDetailsViewModel.joinGame(game) {
                        gameListViewModel.updateSingleGame(game.id)
                    }
                },
                onLeaveClick = { game ->
                    gameDetailsViewModel.leaveGame(game) {
                        gameListViewModel.updateSingleGame(game.id)
                    }
                },
                onDeleteClick = { game ->
                    gameDetailsViewModel.deleteGame(game) {
                        gameListViewModel.removeGame(game.id)
                        fieldListViewModel.removeGameFromField(game.fieldId, game.id)
                    }
                },
                onCardClick  = { game ->
                    navActions.navigateToGame(game.id)
                }
            )

        }
    }
}
