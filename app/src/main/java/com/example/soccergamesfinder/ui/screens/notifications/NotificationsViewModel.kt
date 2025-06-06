// NotificationsViewModel.kt
package com.example.soccergamesfinder.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.model.Notification
import com.example.soccergamesfinder.repository.NotificationsRepository
import com.example.soccergamesfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private var currentListenerJob: Job? = null


    fun startListening(userId: String) {
        // מפסיק האזנה קודמת אם יש
        currentListenerJob?.cancel()
        currentListenerJob = viewModelScope.launch {
            notificationsRepository.listenToNotifications(userId).collectLatest { list ->
                _notifications.value = list
                _unreadCount.value = list.count { !it.isRead }
            }
        }
    }

    fun markAllAsRead() {
        val userId = userRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            notificationsRepository.markAllNotificationsAsRead(userId)
        }
    }



    fun deleteNotification(notificationId: String) {
        val userId = userRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            notificationsRepository.deleteNotification(userId, notificationId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentListenerJob?.cancel()
    }


}
