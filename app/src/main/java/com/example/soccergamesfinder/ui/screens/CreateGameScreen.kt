package com.example.soccergamesfinder.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.CreateGameViewModel
import com.example.soccergamesfinder.viewmodel.FieldsViewModel
import com.example.soccergamesfinder.ui.components.TimeRangeDropdown
import com.example.soccergamesfinder.utils.showDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    fieldId: String?,
    navController: NavController,
    fieldsViewModel: FieldsViewModel,
    authViewModel: AuthViewModel, // כדי לקבל את ה-UID של המשתמש המחובר
    createGameViewModel: CreateGameViewModel = viewModel()
)  {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val selectedDate by createGameViewModel.selectedDate.collectAsState()
    val selectedTimeRange by createGameViewModel.selectedTimeRange.collectAsState()
    val maxPlayers by createGameViewModel.maxPlayers.collectAsState()
    val isGameSaved by createGameViewModel.isGameSaved.collectAsState()
    val field by createGameViewModel.field.collectAsState()
    val isSlotTaken by createGameViewModel.isSlotTaken.collectAsState()
    val userHasGameOnDate by createGameViewModel.userHasGameOnDate.collectAsState()


    if (isGameSaved) {
        LaunchedEffect(Unit) {
            navController.popBackStack() // חזרה למסך הקודם אחרי השמירה
        }
    }

    LaunchedEffect(fieldId) {
        fieldId?.let { id ->
            fieldsViewModel.getFieldById(id)?.let {
                createGameViewModel.setField(it)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("יצירת משחק ב-${field?.name ?: "טוען..."}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "חזור")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("בחר תאריך ושעה למשחק", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            OutlinedButton(
                onClick = { showDatePicker(context, calendar) { createGameViewModel.setDate(it) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selectedDate.isEmpty()) "בחר תאריך" else "תאריך שנבחר: $selectedDate")
            }

            TimeRangeDropdown(selectedTimeRange) { createGameViewModel.setTimeRange(it) }

            OutlinedTextField(
                value = maxPlayers.toString(),
                onValueChange = { createGameViewModel.setMaxPlayers(it.toIntOrNull() ?: 10) },
                label = { Text("מספר שחקנים מקסימלי") },
                modifier = Modifier.fillMaxWidth()
            )

            if (userHasGameOnDate) {
                Text(
                    "משתמש זה כבר יש משחק באותו יום!",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isSlotTaken) {
                Text(
                    "כבר קיים משחק למגרש הזה בזמן שנבחר!",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    fieldId?.let { id ->
                        authViewModel.user.value?.uid?.let { userId ->
                            createGameViewModel.saveGame(id, userId)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDate.isNotEmpty() && selectedTimeRange.isNotEmpty()
            ) {
                Text("שמור משחק")
            }
        }
    }
}