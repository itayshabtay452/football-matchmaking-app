package com.example.soccergamesfinder.ui.components.login

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginButtons(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = onLogin) {
            Text("התחבר")
        }

        Button(onClick = onRegister) {
            Text("הרשמה")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onGoogleLogin) {
            Text("התחבר עם Google")
        }
    }
}
