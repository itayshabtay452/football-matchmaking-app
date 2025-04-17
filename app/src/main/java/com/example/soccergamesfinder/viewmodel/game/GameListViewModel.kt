package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import com.example.soccergamesfinder.data.GameStatus
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameListState())
    val state: StateFlow<GameListState> = _state.asStateFlow()

    init {
        observeGames()
    }

    private fun observeGames() {
        gameRepository.listenToGames(
            onChange = { allGames ->
                val futureGames = allGames
                    .filter { it.status != GameStatus.ENDED }
                    .sortedBy { it.startTime } // אופציונלי

                _state.update {
                    it.copy(games = futureGames, isLoading = false, error = null)
                }
            },
            onError = { e ->
                _state.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        )
    }


}