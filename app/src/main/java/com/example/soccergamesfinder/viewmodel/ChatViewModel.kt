package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadMessages(gameId: String) {
        db.collection("games").document(gameId)
            .collection("chatMessages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                _messages.value = snapshot.documents.mapNotNull { it.toObject(ChatMessage::class.java) }
            }
    }

    fun sendMessage(gameId: String, senderId: String, senderName: String, message: String) {
        if (message.isBlank()) return

        val newMessage = ChatMessage(
            messageId = db.collection("games").document(gameId)
                .collection("chatMessages").document().id,
            senderId = senderId,
            senderName = senderName, // נוסיף את שם השחקן
            message = message,
            timestamp = System.currentTimeMillis()
        )

        db.collection("games").document(gameId)
            .collection("chatMessages")
            .document(newMessage.messageId)
            .set(newMessage)
    }

    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }


}
