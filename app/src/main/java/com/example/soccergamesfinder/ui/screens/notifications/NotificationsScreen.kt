package com.example.soccergamesfinder.ui.screens.notifications

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text("מסך התראות", fontSize = 24.sp)
    }
}
