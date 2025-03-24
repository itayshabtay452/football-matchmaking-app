package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.ui.components.FilterBar
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel,userViewModel: UserViewModel,
               fieldViewModel: FieldViewModel, navigateToLogin: () -> Unit,
                navigateToField: (String) -> Unit
    )
{

    val user by userViewModel.user.collectAsState()
    val fields by fieldViewModel.fields.collectAsState()
    val isLoading by fieldViewModel.isLoading.collectAsState()
    val filterState by fieldViewModel.filterState.collectAsState()


    LaunchedEffect(Unit){
        userViewModel.loadUser()
    }

    LaunchedEffect(user){
        if (user != null)
        {
            fieldViewModel.loadNearbyFields(user?.latitude, user?.longitude)
        }
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        item {
            user?.let {
                Text("👤 שם: ${it.name}", style = MaterialTheme.typography.bodyLarge)
                Text("📛 כינוי: ${it.nickname}", style = MaterialTheme.typography.bodyLarge)
                Text("🎂 גיל: ${it.age}", style = MaterialTheme.typography.bodyLarge)

                it.profileImageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "תמונת פרופיל",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } ?: Text("❌ לא נבחרה תמונת פרופיל")
            } ?: Text("🔄 טוען נתוני משתמש...")
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            FilterBar(
                filterState = filterState,
                onLightingChanged = { fieldViewModel.updateLightingFilter(it) },
                onParkingChanged = { fieldViewModel.updateParkingFilter(it) },
                onFencingChanged = { fieldViewModel.updateFencingFilter(it) },
                onNameQueryChanged = { fieldViewModel.updateNameQuery(it) },
                onSizeChanged = { fieldViewModel.updateSizeFilter(it) },
                onMaxDistanceChanged = {
                    val distance = it.toDoubleOrNull()
                    fieldViewModel.updateMaxDistanceKm(distance)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("מתקנים זמינים", style = MaterialTheme.typography.headlineSmall)
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (fields.isEmpty()) {
            item {
                Text("❌ אין מתקנים זמינים", color = MaterialTheme.colorScheme.error)
            }
        } else {
            items(fields) { field ->
                FieldItem(field, navigateToField)
            }
            item {
                Button(
                    onClick = { fieldViewModel.loadMoreFields() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("🔄 טען עוד")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.logout()
                userViewModel.logout()
                navigateToLogin()
            }) {
                Text("התנתקות")
            }
        }
    }

}

@Composable
fun FieldItem(field: Field, navigateToField: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🏟️ ${field.name ?: "לא ידוע"}", style = MaterialTheme.typography.titleMedium)
            Text("📍 ${field.address ?: "לא ידוע"}")
            Text("📐 גודל: ${field.size ?: "לא ידוע"}")
            Text("🚧 גידור: ${field.fencing ?: "לא ידוע"}")
            Text("💡 תאורה: ${field.lighting ?: "לא ידוע"}")
            Text("🅿️ חניה: ${field.parking ?: "לא ידוע"}")
            Text("📞 טלפון: ${field.phone ?: "לא זמין"}")
            Text("📧 מייל: ${field.email ?: "לא זמין"}")

            field.distance?.let {
                Text("📏 מרחק: ${"%.2f".format(it)} ק\"מ")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navigateToField(field.id) }) {
                Text("🔍 צפה בפרטים")
            }
        }
    }
}
