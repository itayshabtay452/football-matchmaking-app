// CompleteProfileScreen.kt
package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.viewmodel.AuthViewModel

@Composable
fun CompleteProfileScreen(
    authViewModel: AuthViewModel,
    onProfileCompleted: () -> Unit
) {
    val firestore = Firebase.firestore
    val uid = authViewModel.user.value?.uid ?: return

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    var nicknameAvailable by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("השלמת פרטים", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("שם פרטי") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("שם משפחה") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = {
                nickname = it
                nicknameAvailable = true
            },
            label = { Text("כינוי") },
            modifier = Modifier.fillMaxWidth()
        )

        if (!nicknameAvailable) {
            Text("הכינוי תפוס, אנא בחר אחר.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                firestore.collection("users")
                    .whereEqualTo("nickname", nickname)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        nicknameAvailable = snapshot.isEmpty
                        if (nicknameAvailable) {
                            val userData = User(firstName, lastName, nickname)
                            firestore.collection("users").document(uid).set(userData)
                                .addOnSuccessListener { onProfileCompleted() }
                                .addOnFailureListener { errorMessage = it.message }
                        }
                    }
                    .addOnFailureListener { errorMessage = it.message }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("שמור פרטים")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}
