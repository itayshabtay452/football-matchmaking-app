package com.example.soccergamesfinder.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    launchGoogleSignIn: (Intent) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("התחברות", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("אימייל") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("סיסמה") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("התחבר")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launchGoogleSignIn(authViewModel.getGoogleSignInIntent()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("התחבר עם גוגל")
        }

        TextButton(onClick = navigateToRegister) {
            Text("אין לך חשבון? הירשם כאן")
        }

        authViewModel.errorMessage.value?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        LaunchedEffect(authViewModel.user.value) {
            authViewModel.user.value?.let { navigateToHome() }
        }
    }
}
