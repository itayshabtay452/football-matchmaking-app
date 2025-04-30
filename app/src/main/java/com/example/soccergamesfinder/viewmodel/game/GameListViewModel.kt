package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameListState())
    val state: StateFlow<GameListState> = _state.asStateFlow()

    private var hasCheckedEndedGames = false


    init {
        observeGames()
    }

    private fun observeGames() {
        gameRepository.listenToGames(
            onChange = { allGames ->
                if (!hasCheckedEndedGames) {
                    hasCheckedEndedGames = true
                    checkAndHandleEndedGames(allGames) // נבצע את הבדיקה רק פעם אחת
                }

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

    private fun checkAndHandleEndedGames(allGames: List<Game>) {
        val now = System.currentTimeMillis()

        allGames.forEach { game ->
            val gameEndTime = game.endTime.toDate().time

            if (now >= gameEndTime && game.status != GameStatus.ENDED) {
                viewModelScope.launch {
                    gameRepository.moveGameToEndedCollection(game.id)
                    userRepository.removeGameFromAllUsers(game.id)
                    }
                }
            }
        }
    }