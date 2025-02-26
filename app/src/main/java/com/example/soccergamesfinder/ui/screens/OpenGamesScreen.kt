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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.OpenGamesViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.viewmodel.GameWithFieldName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenGamesScreen(
    navController: NavController,
    openGamesViewModel: OpenGamesViewModel = viewModel(),
    userLocation: Location?
) {
    val openGames by openGamesViewModel.openGames.collectAsState()
    val maxDistanceKm by openGamesViewModel.maxDistanceKm.collectAsState()

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
                Text("אין משחקים פתוחים בטווח זה.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(openGames) { gameWithFieldName ->
                        GameCard(gameWithFieldName)
                    }
                }
            }
        }
    }
}



@Composable
fun GameCard(gameWithFieldName: GameWithFieldName) {
    val game = gameWithFieldName.game
    val fieldName = gameWithFieldName.fieldName
    val distance = gameWithFieldName.distanceFromUser

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
            Text("תאריך: ${game.date}", style = MaterialTheme.typography.bodyLarge)
            Text("שעות: ${game.timeRange}", style = MaterialTheme.typography.bodyLarge)
            Text("שחקנים מקסימליים: ${game.maxPlayers}", style = MaterialTheme.typography.bodyLarge)
            distance?.let {
                Text("מרחק: %.1f ק\"מ".format(it), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
