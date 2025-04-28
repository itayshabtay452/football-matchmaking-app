package com.example.soccergamesfinder.ui.screens.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.ui.components.FieldCard
import com.example.soccergamesfinder.ui.components.GameCard

@Composable
fun GameTopSection(
    game: Game,
    field: Field,
    onFieldClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GameHeaderCard(
            game = game,
            modifier = Modifier.weight(1f)
        )
        FieldCard (
            field = field,
            isFollowed = false,
            onFollowClick = {},
            onClick = onFieldClick,
            onCreateGameClick = {}
        )
    }
}