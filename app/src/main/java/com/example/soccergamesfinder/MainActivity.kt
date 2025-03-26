package com.example.soccergamesfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.soccergamesfinder.navigation.AppNavigation
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.LocationViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val userViewModel: UserViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()

            AppNavigation(authViewModel, userViewModel, locationViewModel)
        }
    }
}
