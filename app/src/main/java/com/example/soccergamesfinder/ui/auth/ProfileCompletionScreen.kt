package com.example.soccergamesfinder.ui.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.viewmodel.AuthUiState
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompletionScreen(
    authViewModel: AuthViewModel = viewModel(),
    onProfileCompleteSuccess: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var travelDistance by remember { mutableStateOf("") }
    var freeHours by remember { mutableStateOf("") }
    var skillLevel by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") } // placeholder עבור URI של התמונה

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onProfileCompleteSuccess()
            authViewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // תמונת רקע – ודא שקובץ "login" נמצא ב-res/drawable
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // שכבת overlay שחורה עם שקיפות לשיפור קריאות הטקסט
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        // טופס הפרופיל מוצג במרכז המסך
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "השלם פרופיל",
                style = TextStyle(
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("שם מלא") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("מיקום") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            OutlinedTextField(
                value = travelDistance,
                onValueChange = { travelDistance = it },
                label = { Text("עד כמה אתה מוכן לנסוע (ק\"מ)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            OutlinedTextField(
                value = freeHours,
                onValueChange = { freeHours = it },
                label = { Text("שעות פנוי לשחק") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            SkillLevelDropdown(
                selectedLevel = skillLevel,
                onLevelSelected = { skillLevel = it }
            )
            Button(
                onClick = { /* כאן ניתן להוסיף לוגיקה לבחירת תמונה */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("בחר תמונה", color = MaterialTheme.colorScheme.onPrimary)
            }
            Button(
                onClick = {
                    authViewModel.completeProfile(
                        fullName = fullName,
                        location = location,
                        travelDistance = travelDistance,
                        freeHours = freeHours,
                        skillLevel = skillLevel,
                        imageUri = imageUri
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("שמור פרופיל", color = MaterialTheme.colorScheme.onSecondary)
            }
            if (uiState is AuthUiState.Error) {
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
