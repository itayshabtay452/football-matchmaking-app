package com.example.soccergamesfinder.ui.screens

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
fun OpenGamesScreen(navController: NavController, openGamesViewModel: OpenGamesViewModel = viewModel()) {
    val openGames by openGamesViewModel.openGames.collectAsState()

    LaunchedEffect(Unit) {
        openGamesViewModel.loadOpenGames()
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
            if (openGames.isEmpty()) {
                Text("אין משחקים פתוחים כרגע.", style = MaterialTheme.typography.bodyLarge)
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
        }
    }
}