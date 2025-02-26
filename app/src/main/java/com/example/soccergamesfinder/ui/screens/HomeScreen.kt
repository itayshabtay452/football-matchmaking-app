// HomeScreen.kt (מעוצב בצורה מודרנית ומקצועית עם מיקום משתמש)
package com.example.soccergamesfinder.ui.screens

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel
import com.example.soccergamesfinder.viewmodel.LocationViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    locationViewModel: LocationViewModel,
    navigateToLogin: () -> Unit,
    navigateToFieldsList: () -> Unit,
    navigateToCreateGame: () -> Unit,
    navigateToOpenGames: () -> Unit,
    navigateToCompleteProfile: () -> Unit
) {
    val user by userViewModel.currentUser
    val location by locationViewModel.currentLocation.collectAsState()
    val context = LocalContext.current
    var address by remember { mutableStateOf("טוען מיקום...") }

    LaunchedEffect(location) {
        location?.let { loc ->
            val geocoder = Geocoder(context, Locale("he"))
            if (Geocoder.isPresent()) {
                geocoder.getFromLocation(loc.latitude, loc.longitude, 1,
                    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            address = addresses.firstOrNull()?.getAddressLine(0) ?: "לא ניתן לאתר את הכתובת"
                        }

                        override fun onError(errorMessage: String?) {
                            address = "שגיאה בקבלת הכתובת"
                        }
                    })
            } else {
                address = "Geocoder לא זמין"
            }
        } ?: run {
            address = "המיקום לא זמין"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // כרטיס משתמש
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                user?.let {
                    Image(
                        painter = painterResource(id = R.drawable.home), // שנה לתמונה רלוונטית
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = "שלום, ${it.firstName} ${it.lastName}!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                } ?: CircularProgressIndicator(color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // כפתורי ניווט
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeButton("📍 עדכן מיקום", Color(0xFF42A5F5)) {
                locationViewModel.requestLocation()
            }
            HomeButton("⚽ רשימת המגרשים", Color(0xFFAB47BC)) {
                navigateToFieldsList()
            }
            HomeButton("🎮 יצירת משחק חדש", Color(0xFFFF7043)) {
                navigateToCreateGame()
            }
            HomeButton("🏆 משחקים פתוחים", Color(0xFF66BB6A)) {
                navigateToOpenGames()
            }
            HomeButton("✍️ השלם את הפרופיל", Color(0xFFFFD54F)) {
                navigateToCompleteProfile()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // כפתור התנתקות
        OutlinedButton(
            onClick = {
                authViewModel.logout()
                navigateToLogin()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("🚪 התנתק", fontWeight = FontWeight.Bold)
        }
    }
}

// קומפוננטת כפתור מעוצב
@Composable
fun HomeButton(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
