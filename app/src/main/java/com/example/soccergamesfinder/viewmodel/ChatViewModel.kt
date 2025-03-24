package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.ChatMessage
import com.example.soccergamesfinder.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _massages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val massages: StateFlow<List<ChatMessage>> = _massages

    private val _isSendingMessage = MutableStateFlow(false)
    val isSendingMessage: StateFlow<Boolean> = _isSendingMessage

    fun loadMassages(gameId: String) {
        viewModelScope.launch {
            chatRepository.listenToMessages(gameId).collect {
                _massages.value = it
            }
        }
    }

    fun sendMessage(gameId: String, message: ChatMessage) {
        viewModelScope.launch {
            _isSendingMessage.value = true
            chatRepository.sendMessage(gameId, message)
            _isSendingMessage.value = false
        }
    }

}