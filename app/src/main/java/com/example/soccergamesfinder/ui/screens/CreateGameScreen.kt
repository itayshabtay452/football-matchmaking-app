package com.example.soccergamesfinder.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.viewmodel.CreateGameViewModel
import com.example.soccergamesfinder.viewmodel.FieldsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    fieldId: String?,
    navController: NavController,
    fieldsViewModel: FieldsViewModel,
    createGameViewModel: CreateGameViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val selectedDate by createGameViewModel.selectedDate.collectAsState()
    val selectedTimeRange by createGameViewModel.selectedTimeRange.collectAsState()
    val field by createGameViewModel.field.collectAsState()

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

            // בחירת תאריך
            OutlinedButton(
                onClick = { showDatePicker(context, calendar) { createGameViewModel.setDate(it) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selectedDate.isEmpty()) "בחר תאריך" else "תאריך שנבחר: $selectedDate")
            }

            // בחירת טווח שעות
            TimeRangeDropdown(selectedTimeRange) { createGameViewModel.setTimeRange(it) }

            Button(
                onClick = { /* לשמור את המשחק ב-Firebase */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDate.isNotEmpty() && selectedTimeRange.isNotEmpty()
            ) {
                Text("שמור משחק")
            }
        }
    }
}




// פונקציה להצגת תיבת דיאלוג לבחירת תאריך
fun showDatePicker(context: Context, calendar: Calendar, onDateSelected: (String) -> Unit) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(sdf.format(selectedCalendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

@Composable
fun TimeRangeDropdown(selectedTimeRange: String, onTimeRangeSelected: (String) -> Unit) {
    val timeRanges = listOf("16:00-18:00", "18:00-20:00", "20:00-22:00", "22:00-24:00")
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedTimeRange.isEmpty()) "בחר טווח שעות" else "שעות שנבחרו: $selectedTimeRange")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            timeRanges.forEach { range ->
                DropdownMenuItem(
                    text = { Text(range) },
                    onClick = {
                        onTimeRangeSelected(range)
                        expanded = false
                    }
                )
            }
        }
    }
}
