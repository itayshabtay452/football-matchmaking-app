package com.example.soccergamesfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        // נניח שיש לכם תמונת רקע בשם "home" ב-res/drawable, ניתן להחליף לכל תמונה אחרת.
        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // שכבת overlay עם רקע שחור שקוף, לשמירה על אחידות קריאות הטקסט
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        // תיבות הטקסט והכפתורים ממוקמות במרכז עם padding אחיד
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // כותרת למסך הבית
            Text(
                text = "דף הבית",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
            )

            // כפתור התנתקות
            Button(
                onClick = {
                    homeViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("התנתק")
            }

            // כפתור לעריכת פרופיל
            Button(
                onClick = { navController.navigate("edit_profile_screen") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ערוך פרופיל")
            }

            // כפתור להצגת משחקים פתוחים באזור המשתמש
            Button(
                onClick = { navController.navigate("open_games_screen") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("משחקים פתוחים")
            }

            // כפתור לפתיחת מגרש חדש
            Button(
                onClick = { navController.navigate("new_game_screen") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("פתח מגרש חדש")
            }
        }
    }
}
