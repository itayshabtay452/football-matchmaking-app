// HomeScreen.kt – גרסה מודרנית בעיצוב קלאסי

package com.example.soccergamesfinder.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.*
import com.example.soccergamesfinder.viewmodel.field.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.game.GameDetailsViewModel
import com.example.soccergamesfinder.viewmodel.game.GameListViewModel
import com.example.soccergamesfinder.viewmodel.user.CurrentUserViewModel

@Composable
fun HomeScreen(
    navActions: HomeScreenNavActions,
    fieldListViewModel: FieldListViewModel,
    gameListViewModel: GameListViewModel,
    currentUserViewModel: CurrentUserViewModel,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopStatsSection(
            onAddGameClick = {  },
            onLogoutClick = { currentUserViewModel.signOut() }
        )
        FieldCarousel(
            fields = fields.take(5),
            followedFields = fieldState.followedFields,
            onFollowFieldClick = { fieldListViewModel.toggleFollowField(it.id) },
            onFieldClick = { navActions.navigateToField(it.id) },
            onCreateGame = { field ->
            }
        )
        GameCarousel(
            games = games.take(5),
            fields = fields,
            currentUser = currentUser,
            onJoinClick = { gameDetailsViewModel.joinGame(it) },
            onLeaveClick = { gameDetailsViewModel.leaveGame(it) },
            onDeleteClick = { gameDetailsViewModel.deleteGame(it) },
            onCardClick = { navActions.navigateToGame(it.id) }
        )
    }
}
