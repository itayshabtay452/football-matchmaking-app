package com.example.soccergamesfinder.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.model.Message
import com.example.soccergamesfinder.repository.ChatRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class ChatState(
    val messages: List<Message> = emptyList(),
    val currentMessage: String = "",
    val isLoading: Boolean = false,
    val currentUserId: String = "",
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private var messageListener: ListenerRegistration? = null

    init {
        viewModelScope.launch {
            val currentUserId = userRepository.getCurrentUserId()
            if (currentUserId != null) {
                _state.update { it.copy(currentUserId = currentUserId) }
            }
        }
    }

    fun startListening(gameId: String) {
        messageListener?.remove()
        messageListener = chatRepository.listenToMessages(
            gameId = gameId,
            onChange = { messages ->
                _state.update { it.copy(messages = messages, isLoading = false, error = null) }
            },
            onError = { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        )
    }

    fun onMessageChange(text: String) {
        _state.update { it.copy(currentMessage = text) }
    }

    fun sendMessage(gameId: String) {
        val text = _state.value.currentMessage.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            val senderId = userRepository.getCurrentUserId() ?: return@launch
            val senderName = userRepository.getUserById(senderId)?.nickname ?: "אנונימי"
            val message = Message(
                id = UUID.randomUUID().toString(),
                gameId = gameId,
                senderId = senderId,
                senderName = senderName,
                text = text,
                timestamp = System.currentTimeMillis()
            )

            val result = chatRepository.sendMessage(gameId, message)
            if (result.isSuccess) {
                _state.update { it.copy(currentMessage = "") }
            } else {
                _state.update { it.copy(error = "שליחת ההודעה נכשלה") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageListener?.remove()
    }
}
