package com.example.soccergamesfinder.viewmodel.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FieldDetailsState(
    val field: Field? = null,
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FieldDetailsViewModel @Inject constructor(
    private val fieldRepository: FieldRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FieldDetailsState())
    val state: StateFlow<FieldDetailsState> = _state.asStateFlow()

    fun loadFieldDetails(fieldId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val field = fieldRepository.getFieldById(fieldId)
            if (field == null) {
                _state.update { it.copy(isLoading = false, error = "Field not found") }
                return@launch
            }

            val games = gameRepository.getGamesByFieldId(fieldId)

            _state.update {
                it.copy(
                    isLoading = false,
                    field = field,
                    games = games
                )
            }
        }
    }
}
