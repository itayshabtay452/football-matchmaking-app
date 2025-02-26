// AppNavigation.kt
package com.example.soccergamesfinder.ui.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soccergamesfinder.ui.screens.*
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldsViewModel
import com.example.soccergamesfinder.viewmodel.LocationViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    locationViewModel: LocationViewModel,
    launchGoogleSignIn: (Intent) -> Unit
) {
    val navController = rememberNavController()
    val fieldsViewModel: FieldsViewModel = viewModel() // ViewModel משותף לכל המסכים

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                navigateToHome = {
                    authViewModel.user.value?.uid?.let { uid ->
                        Firebase.firestore.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    userViewModel.fetchUserData()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    navController.navigate("completeProfile") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                    }
                },
                navigateToRegister = { navController.navigate("register") },
                launchGoogleSignIn = launchGoogleSignIn
            )
        }


        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                navigateToCompleteProfile = { navController.navigate("completeProfile") }
            )
        }

        composable("completeProfile") {
            CompleteProfileScreen(authViewModel = authViewModel) {
                userViewModel.fetchUserData()
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable("home") {
            HomeScreen(
                userViewModel = userViewModel,
                authViewModel = authViewModel,
                locationViewModel = locationViewModel,
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                navigateToFieldsList = { navController.navigate("fieldsList") },
                navigateToCreateGame = { navController.navigate("createGame") },
                navigateToOpenGames = { navController.navigate("openGames") },
                navigateToCompleteProfile = { navController.navigate("completeProfile") }
            )
        }

        composable("fieldsList") {
            val locationViewModel: LocationViewModel = viewModel()

            val userLocation by locationViewModel.currentLocation.collectAsState()

            FieldsListScreen(
                fieldsViewModel = fieldsViewModel,
                userLocation = userLocation,
                navController = navController
            )
        }

        composable("createGameScreen/{fieldId}") { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getString("fieldId")
            CreateGameScreen(fieldId = fieldId,
                navController = navController,
                fieldsViewModel = fieldsViewModel,
                authViewModel = authViewModel,
                createGameViewModel = viewModel())
        }

        composable("openGames") {
            val locationViewModel: LocationViewModel = viewModel()
            val userLocation by locationViewModel.currentLocation.collectAsState()

            OpenGamesScreen(navController = navController, userLocation = userLocation)
        }







    }
}
