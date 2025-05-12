// FavoritesScreen.kt
package com.example.soccergamesfinder.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.FieldCarousel
import com.example.soccergamesfinder.ui.components.GameCarousel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@Composable
fun FavoritesScreen(
    currentUserViewModel: CurrentUserViewModel,
    fieldListViewModel: FieldListViewModel,
    gameListViewModel: GameListViewModel,
    onNavigateToGame: (String) -> Unit,
    onNavigateToField: (String) -> Unit
) {
    val currentUserState by currentUserViewModel.state.collectAsState()
    val followedFieldIds = currentUserState.user?.fieldsFollowed ?: emptyList()
    val followedGameIds = currentUserState.user?.gamesFollowed ?: emptyList()

    val allFieldsState by fieldListViewModel.state.collectAsState()
    val allGamesState by gameListViewModel.state.collectAsState()

    val gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()

    val favoriteFields = remember(allFieldsState.fields, followedFieldIds) {
        allFieldsState.fields.filter { it.id in followedFieldIds }
    }

    val favoriteGames = remember(allGamesState.games, followedGameIds) {
        allGamesState.games.filter { it.id in followedGameIds }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("🏟️ המגרשים אחריהם אתה עוקב", style = MaterialTheme.typography.titleMedium)
        }

        item {
            if (favoriteFields.isEmpty()) {
                Text("אין מגרשים במועדפים")
            } else {
                FieldCarousel(
                    fields = favoriteFields,
                    followedFields = followedFieldIds,
                    onFollowFieldClick = { fieldListViewModel.toggleFollowField(it.id) },
                    onFieldClick = { onNavigateToField(it.id) },
                    onCreateGame = { /* TODO */ }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("⚽ המשחקים העתידיים שלך", style = MaterialTheme.typography.titleMedium)
        }

        item {
            if (favoriteGames.isEmpty()) {
                Text("אין משחקים במועדפים")
            } else {
                GameCarousel(
                    games = favoriteGames,
                    fields = allFieldsState.fields,
                    currentUser = currentUserState.user,
                    onJoinClick = { gameDetailsViewModel.joinGame(it) },
                    onLeaveClick = { gameDetailsViewModel.leaveGame(it) },
                    onDeleteClick = { gameDetailsViewModel.deleteGame(it) },
                    onCardClick = { onNavigateToGame(it.id) }
                )
            }
        }
    }
}