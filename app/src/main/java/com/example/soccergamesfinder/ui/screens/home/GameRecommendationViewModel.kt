package com.example.soccergamesfinder.viewmodel.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.ai.GameWithDetails
import com.example.soccergamesfinder.ai.buildGameRecommendationPrompt
import com.example.soccergamesfinder.model.*
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.services.GptService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.soccergamesfinder.services.LocationService

data class GameRecommendationState(
    val isLoading: Boolean = false,
    val recommendation: String? = null,
    val recommendedGame: Game? = null,
    val error: String? = null
)

@HiltViewModel
class GameRecommendationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val gptService: GptService,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(GameRecommendationState())
    val state: StateFlow<GameRecommendationState> = _state.asStateFlow()

    fun recommendBestGame(
        currentUser: User,
        games: List<Game>,
        fields: List<Field>
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                val address = locationService.getAddressFromLocation(
                    latitude = currentUser.latitude,
                    longitude = currentUser.longitude
                )


                val gamesWithDetails = games.mapNotNull { game ->
                    val field = fields.find { it.id == game.fieldId } ?: return@mapNotNull null
                    val joinedPlayers = userRepository.getUsersByIds(game.joinedPlayers)
                    GameWithDetails(game, field, joinedPlayers)
                }

                val prompt = buildGameRecommendationPrompt(currentUser.copy(address = address), gamesWithDetails)

                val response = gptService.sendPrompt(prompt)

                println("GPT Response: $response")

                val idRegex = Regex("ID[:\\s]+([a-zA-Z0-9\\-]+)")
                val match = idRegex.find(response)
                val gameId = match?.groupValues?.get(1)

                val recommendedGame = gameId?.let { id ->
                    gamesWithDetails.find { it.game.id == id }?.game
                }


                _state.value = _state.value.copy(
                    isLoading = false,
                    recommendation = response,
                    recommendedGame = recommendedGame
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "שגיאה לא ידועה"
                )
            }
        }
    }
}
