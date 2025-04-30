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
import com.example.soccergamesfinder.ui.components.FieldCard
import com.example.soccergamesfinder.ui.components.FieldCarousel
import com.example.soccergamesfinder.ui.components.GameCard
import com.example.soccergamesfinder.ui.components.GameCarousel
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

enum class FavoritesTab { Fields, Games }

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
        allFieldsState.fields.filter { followedFieldIds.contains(it.id) }
    }

    val favoriteGames = remember(allGamesState.games, followedGameIds) {
        allGamesState.games.filter { followedGameIds.contains(it.id) }
    }

    var selectedTab by remember { mutableStateOf(FavoritesTab.Fields) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedTab == FavoritesTab.Fields,
                onClick = { selectedTab = FavoritesTab.Fields },
                label = { Text("מגרשים") }
            )
            FilterChip(
                selected = selectedTab == FavoritesTab.Games,
                onClick = { selectedTab = FavoritesTab.Games },
                label = { Text("משחקים") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            FavoritesTab.Fields -> {
                if (favoriteFields.isEmpty()) {
                    Text("אין מגרשים במועדפים")
                } else {
                    FieldCarousel(
                        fields = favoriteFields,
                        followedFields = followedFieldIds,
                        onFollowFieldClick = { fieldListViewModel.toggleFollowField(it.id) },
                        onFieldClick = { onNavigateToField(it.id) },
                        onCreateGame = { field ->
                        }
                    )
                }
            }
            FavoritesTab.Games -> {
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
}