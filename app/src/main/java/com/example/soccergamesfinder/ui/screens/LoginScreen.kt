package com.example.soccergamesfinder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel


@Composable
fun LoginScreen(authViewModel: AuthViewModel,userViewModel: UserViewModel, navigateToHome: () -> Unit,
                navigateToCompleteProfile: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val user by authViewModel.user.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val userExists by userViewModel.userExists.collectAsState()



    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authViewModel.handleGoogleSignInResult(result.data)
    }

    LaunchedEffect(user) {
        if (user != null){
            authViewModel.clearErrorMessage()
            userViewModel.checkIfUserExists()
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
        TextField(value = email, onValueChange = { email = it }, label = { Text("אימייל") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("סיסמה") })
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = { authViewModel.login(email, password) }) {
            Text("התחבר")
        }
        Button(onClick = { authViewModel.register(email, password) }) {
            Text("הרשמה")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { googleSignInLauncher.launch(authViewModel.getGoogleSignInIntent()) }) {
            Text("התחבר עם Google")
        }
    }

}
