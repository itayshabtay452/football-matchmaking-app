package com.example.soccergamesfinder.ui.components.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun LogoutButton(authViewModel: AuthViewModel, userViewModel: UserViewModel,
                 onLogout: () -> Unit) {

    Button(onClick = {
        authViewModel.logout()
        userViewModel.logout()
        onLogout()
    }) {
        Text("התנתקות")
    }

}
