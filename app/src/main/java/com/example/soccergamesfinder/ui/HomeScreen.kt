package com.example.soccergamesfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // רקע עם תמונה
        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // שכבת overlay עם gradient כהה
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        )
        // לוח שקוף (glass effect) ללא שימוש ב־Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center)
                .background(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "דף הבית",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                ElevatedButton(
                    onClick = {
                        homeViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text("התנתק", color = Color.White)
                }
                ElevatedButton(
                    onClick = { navController.navigate("profile_details_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text("צפה בפרופיל", color = Color.White)
                }
                ElevatedButton(
                    onClick = { navController.navigate("fields_list_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text("מצא מגרשי כדורגל", color = Color.White)
                }
                ElevatedButton(
                    onClick = { navController.navigate("new_game_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text("פתח מגרש חדש", color = Color.White)
                }
            }
        }
    }
}
