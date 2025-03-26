package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.field.FieldHeaderSection
import com.example.soccergamesfinder.ui.components.field.GameListSection
import com.example.soccergamesfinder.viewmodel.FieldDetailsViewModel
import com.example.soccergamesfinder.viewmodel.GameViewModel

@Composable
fun FieldScreen(
    fieldId: String,
    navigateToCreateGame: (String) -> Unit, navigateToGame: (String) -> Unit
) {

    val fieldDetailsViewModel: FieldDetailsViewModel = hiltViewModel()
    val gameViewModel: GameViewModel = hiltViewModel()

    val uiState by fieldDetailsViewModel.uiState.collectAsState()
    val games by gameViewModel.games.collectAsState()

    LaunchedEffect(fieldId) {
        fieldDetailsViewModel.loadField(fieldId)
        gameViewModel.loadGames(fieldId)
    }

        Column(modifier = Modifier.padding(16.dp)) {

            FieldHeaderSection(uiState)

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