package com.example.soccergamesfinder.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.soccergamesfinder.ui.components.*
import com.example.soccergamesfinder.viewmodel.ai.GameRecommendationViewModel
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
    var showDevScreen by remember { mutableStateOf(false) }

    if (showDevScreen) {
        DevToolsScreen(onBack = { showDevScreen = false })
        return
    }

    val userState = currentUserViewModel.state.collectAsState().value
    val fieldState = fieldListViewModel.state.collectAsState().value
    val gameState = gameListViewModel.state.collectAsState().value

    val gameRecommendationViewModel: GameRecommendationViewModel = hiltViewModel()
    val recommendationState = gameRecommendationViewModel.state.collectAsState().value

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentUser != null) {
                MiniProfileCard(
                    nickname = currentUser.nickname,
                    profileImageUrl = currentUser.profileImageUrl,
                    onClick = { navActions.onNavigateToUser(currentUser.id) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { navActions.navigateToAddField() }) {
                Text("הוסף מגרש")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { currentUserViewModel.signOut() }) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "התנתק")
            }

        }


        Text(
            text = "⚽ מגרשים בסביבה שלך",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        FieldCarousel(
            fields = fields.take(5),
            followedFields = fieldState.followedFields,
            onFollowFieldClick = { fieldListViewModel.toggleFollowField(it.id) },
            onFieldClick = { navActions.navigateToField(it.id) },
            onCreateGame = { field -> }
        )

        Text(
            text = "🎯 משחקים קרובים",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🤖 רוצה עזרה במציאת משחק?",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = {
                currentUser?.let {
                    gameRecommendationViewModel.recommendBestGame(it, games, fields.take(30))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("מצא לי משחק שמתאים לי")
        }

        if (recommendationState.isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (recommendationState.error != null) {
            Text(
                text = "שגיאה: ${recommendationState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            recommendationState.recommendedGame?.let { game ->
                GameCarousel(
                    games = listOf(game),
                    fields = fields,
                    currentUser = currentUser,
                    onJoinClick = { gameDetailsViewModel.joinGame(it) },
                    onLeaveClick = { gameDetailsViewModel.leaveGame(it) },
                    onDeleteClick = { gameDetailsViewModel.deleteGame(it) },
                    onCardClick = { navActions.navigateToGame(it.id) }
                )
            }
        }
    }
}


@Composable
fun MiniProfileCard(
    nickname: String,
    profileImageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .height(60.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = "שלום, $nickname 👋",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
