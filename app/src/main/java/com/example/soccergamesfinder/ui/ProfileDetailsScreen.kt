package com.example.soccergamesfinder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.ProfileDetailsViewModel

@Composable
fun ProfileDetailsScreen(
    navController: NavController,
    viewModel: ProfileDetailsViewModel = viewModel()
) {
    val profileState = viewModel.profile.collectAsState()
    val profile = profileState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF1976D2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // לוח שקוף להצגת פרטי המשתמש
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "פרטי המשתמש",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)
                    ProfileField(label = "שם", value = profile.firstName)
                    ProfileField(label = "שם משפחה", value = profile.lastName)
                    ProfileField(label = "גיל", value = profile.selectedAge.toString())
                    ProfileField(label = "כינוי", value = profile.nickName)
                }
            }
            // כפתור חזרה למסך הבית
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))
            ) {
                Text("חזרה למסך הבית", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}
