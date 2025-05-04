package com.example.soccergamesfinder.ui.screens.addfieldchat

data class AddFieldChatState(
    val currentQuestion: FieldQuestion = FieldQuestion.Name,

    // תשובות שהמשתמש נתן
    val name: String? = null,
    val address: String? = null,
    val size: String? = null,
    val lighting: Boolean? = null,
    val description: String? = null,

    // תצוגת הצ'אט בפועל (רשימת הודעות)
    val messages: List<ChatMessage> = emptyList(),

    // מצבים זמניים
    val awaitingUserInput: Boolean = false,
    val showNameInputField: Boolean = false
)

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)