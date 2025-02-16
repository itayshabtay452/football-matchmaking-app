package com.example.soccergamesfinder.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.ui.components.InputFieldWithError
import com.example.soccergamesfinder.ui.components.AgeDropdown
import com.example.soccergamesfinder.viewmodel.ProfileViewModel

@Composable
fun ProfileCompletionScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    onProfileCompleteSuccess: () -> Unit = {}
) {
    // קריאה למצב הטופס מה-ViewModel (ProfileFormState)
    val formState = profileViewModel.profileFormState.collectAsState().value
    val saveSuccess = profileViewModel.profileSaveSuccess.collectAsState().value

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onProfileCompleteSuccess()
            profileViewModel.resetProfileSaveSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // תמונת רקע (נניח שיש לך תמונה בשם "login" ב-res/drawable)
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // שכבת overlay שחורה עם שקיפות
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        // הטופס במרכז – עטיפה בגלילה, עם Padding והתאמה מלאה לרוחב
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

            // שדה "שם פרטי"
            InputFieldWithError(
                value = formState.firstName,
                onValueChange = { profileViewModel.onFirstNameChanged(it) },
                label = "שם פרטי",
                error = formState.firstNameError,
                modifier = Modifier.fillMaxWidth()
            )

            // שדה "שם משפחה"
            InputFieldWithError(
                value = formState.lastName,
                onValueChange = { profileViewModel.onLastNameChanged(it) },
                label = "שם משפחה",
                error = formState.lastNameError,
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown לבחירת גיל (טווח 14–60)
            AgeDropdown(
                selectedAge = formState.selectedAge,
                onAgeSelected = { profileViewModel.onAgeChanged(it) },
                error = formState.ageError,
                modifier = Modifier.fillMaxWidth()
            )

            // שדה "כינוי"
            InputFieldWithError(
                value = formState.nickName,
                onValueChange = { profileViewModel.onNickNameChanged(it) },
                label = "כינוי",
                error = formState.nickNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        // כאשר השדה מאבד פוקוס, נבצע בדיקה לייחודיות
                        if (!focusState.isFocused) {
                            profileViewModel.checkNicknameUnique(formState.nickName)
                        }
                    }
            )

            // כפתור "שמור פרופיל"
            Button(
                onClick = { profileViewModel.saveProfile() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("שמור פרופיל", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}
