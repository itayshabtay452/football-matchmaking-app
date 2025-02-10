package com.example.soccergamesfinder.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.viewmodel.AuthUiState
import com.example.soccergamesfinder.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompletionScreen(
    authViewModel: AuthViewModel = viewModel(),
    onProfileCompleteSuccess: () -> Unit
) {
    // ניהול מצב ה-UI דרך ה-ViewModel
    val uiState by authViewModel.uiState.collectAsState()

    // שדות הפרופיל החדשים
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") } // כאן ניתן לשמור את נתיב התמונה

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onProfileCompleteSuccess()
            authViewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // רקע – ודא שיש לך תמונה בשם "login" ב-res/drawable
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // שכבת overlay עם רקע שחור שקוף לשיפור קריאות הטקסט
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        // עטיפת התוכן בגלילה כדי שלא ייחתך, במקרה שהמסך ארוך
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "השלם פרופיל",
                style = TextStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.primary)
            )
            // שדה שם פרטי
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("שם פרטי") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            // שדה שם משפחה
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("שם משפחה") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            // שדה גיל
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("גיל") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            // שדה כינוי
            OutlinedTextField(
                value = nickName,
                onValueChange = { nickName = it },
                label = { Text("כינוי") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White
                )
            )
            // שדה מיקום
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
            // כפתור לבחירת תמונה – ניתן להוסיף לוגיקה לבחירת תמונה מהגלריה או מצלמה
            Button(
                onClick = { /* הוסף כאן לוגיקה לבחירת תמונה */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("בחר תמונה", color = MaterialTheme.colorScheme.onPrimary)
            }
            // כפתור לשמירת הפרופיל
            Button(
                onClick = {
                    // קריאה לפונקציה להשלמת פרופיל עם השדות החדשים
                    authViewModel.completeProfile(
                        firstName = firstName,
                        lastName = lastName,
                        age = age,
                        nickName = nickName,
                        location = location,
                        imageUri = imageUri
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("שמור פרופיל", color = MaterialTheme.colorScheme.onSecondary)
            }
            // הצגת הודעת שגיאה אם קיימת
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
