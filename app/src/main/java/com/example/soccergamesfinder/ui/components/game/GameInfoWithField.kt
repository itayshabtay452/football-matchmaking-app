package com.example.soccergamesfinder.ui.components.game

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.viewmodel.FieldDetailsUiState
import com.example.soccergamesfinder.data.User

@Composable
fun GameInfoWithField(
    game: Game,
    uiState: FieldDetailsUiState,
    creator: User?,
    participants: List<User>
) {
    when (uiState) {
        is FieldDetailsUiState.Loading -> Text("טוען מידע על המגרש...")
        is FieldDetailsUiState.Error -> Text("❌ שגיאה בטעינת פרטי המגרש")
        is FieldDetailsUiState.Success -> GameInfoSection(game, uiState.field, creator, participants)
    }
}
