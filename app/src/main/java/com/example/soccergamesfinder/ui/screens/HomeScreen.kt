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


    LaunchedEffect(Unit){
        userViewModel.loadUser()
    }

    LaunchedEffect(user){
        if (user != null)
        {
            fieldViewModel.loadNearbyFields(user?.latitude, user?.longitude)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        user?.let {
            Text(text = "👤 שם: ${it.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "📛 כינוי: ${it.nickname}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "🎂 גיל: ${it.age}", style = MaterialTheme.typography.bodyLarge)

            it.profileImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "תמונת פרופיל",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            } ?: Text(text = "❌ לא נבחרה תמונת פרופיל")
        } ?: Text(text = "🔄 טוען נתוני משתמש...")


        Spacer(modifier = Modifier.height(16.dp))

        Text("מתקנים זמינים", style = MaterialTheme.typography.headlineSmall)

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> {
                    // 🌀 טוען...
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                fields.isEmpty() -> {
                    // ❌ אין מתקנים
                    Text("❌ אין מתקנים זמינים", color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                }
            }
        }

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

@Composable
fun FieldItem(field: Field, navigateToField: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            field.name?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            Text(text = "גודל: ${field.size}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "כתובת: ${field.address}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {navigateToField(field.id)}) { Text("צפה בפרטים") }
        }
    }
}
