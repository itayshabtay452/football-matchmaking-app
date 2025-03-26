package com.example.soccergamesfinder.ui.components.field

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.soccergamesfinder.viewmodel.FieldDetailsUiState

@Composable
fun FieldHeaderSection(uiState: FieldDetailsUiState) {
    when (uiState) {
        is FieldDetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is FieldDetailsUiState.Error -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("❌ שגיאה בטעינת פרטי המגרש")
            }
        }
        is FieldDetailsUiState.Success -> {
            FieldHeader(field = uiState.field)
        }
    }
}
