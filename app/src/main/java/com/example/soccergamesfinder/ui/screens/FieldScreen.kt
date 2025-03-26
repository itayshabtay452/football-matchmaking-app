package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.field.FieldHeader
import com.example.soccergamesfinder.ui.components.field.GameListSection
import com.example.soccergamesfinder.viewmodel.FieldViewModel
import com.example.soccergamesfinder.viewmodel.GameViewModel

@Composable
fun FieldScreen(
    fieldId: String, fieldViewModel: FieldViewModel,
    navigateToCreateGame: (String) -> Unit, navigateToGame: (String) -> Unit
) {

    val gameViewModel: GameViewModel = hiltViewModel()

    val field by fieldViewModel.field.collectAsState()
    val games by gameViewModel.games.collectAsState()
    val isLoading by fieldViewModel.isLoading.collectAsState()

    LaunchedEffect(fieldId) {
        fieldViewModel.loadField(fieldId)
        gameViewModel.loadGames(fieldId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {

            FieldHeader(field)

            Spacer(modifier = Modifier.height(16.dp))

            Text("🎮 משחקים פתוחים:", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            GameListSection(games = games, onGameClick = navigateToGame)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navigateToCreateGame(fieldId) }) {
                Text("➕ צור משחק חדש")
            }
        }
    }
}