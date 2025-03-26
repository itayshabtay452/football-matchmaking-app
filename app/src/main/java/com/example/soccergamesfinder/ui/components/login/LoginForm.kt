package com.example.soccergamesfinder.ui.components.login

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("אימייל") },
        modifier = modifier
    )
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("סיסמה") },
        modifier = modifier
    )
}
