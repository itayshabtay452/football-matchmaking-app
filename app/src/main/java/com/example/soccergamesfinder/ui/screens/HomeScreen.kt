package com.example.soccergamesfinder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        fieldViewModel.loadFields(reset = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("ברוך הבא לאפליקציה!", style = MaterialTheme.typography.headlineMedium)
        Text("שם: ${user?.name ?: "לא זמין"}")
        Text("עיר: ${user?.city ?: "לא זמין"}")
        Text("גיל: ${user?.age ?: "לא ידוע"}")


        Spacer(modifier = Modifier.height(16.dp))

        Text("מתקנים זמינים", style = MaterialTheme.typography.headlineSmall)

        Box(modifier = Modifier.weight(1f)) {
            if (fields.isEmpty()) {
                Text("❌ אין מתקנים זמינים", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(fields) { field ->
                        FieldItem(field, navigateToField)
                    }
                    item {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        } else {
                            Button(
                                onClick = { fieldViewModel.loadFields() },
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
            fieldViewModel.resetFields()
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
            Text(text = field.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "גודל: ${field.size}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "כתובת: ${field.address}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {navigateToField(field.id)}) { Text("צפה בפרטים") }
        }
    }
}
