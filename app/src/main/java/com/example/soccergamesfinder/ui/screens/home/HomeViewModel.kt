package com.example.soccergamesfinder.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun updateState(
        userNickname: String,
        userProfileImageUrl: String?,
        fields: List<Field>,
        games: List<Game>,
        isLoading: Boolean,
        error: String?
    ) {
        _state.value = HomeState(
            userNickname = userNickname,
            userProfileImageUrl = userProfileImageUrl,
            fields = fields.take(5),
            games = games.take(5),
            isLoading = isLoading,
            error = error
        )
    }
}
