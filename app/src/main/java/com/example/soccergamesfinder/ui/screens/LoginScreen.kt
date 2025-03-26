package com.example.soccergamesfinder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.ui.components.login.ErrorMessage
import com.example.soccergamesfinder.ui.components.login.LoginButtons
import com.example.soccergamesfinder.ui.components.login.LoginForm
import com.example.soccergamesfinder.viewmodel.AuthViewModel


@Composable
fun LoginScreen(authViewModel: AuthViewModel, navigateToHome: () -> Unit,
                navigateToCompleteProfile: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val user by authViewModel.user.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val userExists by authViewModel.userExists.collectAsState()



    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authViewModel.handleGoogleSignInResult(result.data)
    }

    LaunchedEffect(user) {
        if (user != null){
            authViewModel.checkIfUserExists()
        }
    }

    LaunchedEffect(userExists) {
        if (userExists!= null){
            if (userExists == true){
                navigateToHome()
            }
            else
                navigateToCompleteProfile()
        }
    }


    Column(modifier = Modifier.padding(16.dp)) {
        LoginForm(
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it }
        )
        Spacer(modifier = Modifier.height(8.dp))

        ErrorMessage(message = errorMessage)
        Spacer(modifier = Modifier.height(8.dp))

        LoginButtons(
            onLogin = { authViewModel.login(email, password) },
            onRegister = { authViewModel.register(email, password) },
            onGoogleLogin = {
                googleSignInLauncher.launch(authViewModel.getGoogleSignInIntent())
            }
        )
    }

}
