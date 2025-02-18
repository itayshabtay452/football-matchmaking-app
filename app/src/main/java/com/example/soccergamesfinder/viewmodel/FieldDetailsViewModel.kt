package com.example.soccergamesfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Model representing a field
data class FieldDetails(
    val name: String = ""
)

class FieldDetailsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _fieldDetails = MutableStateFlow<FieldDetails?>(null)
    val fieldDetails: StateFlow<FieldDetails?> = _fieldDetails


    fun loadFieldDetails(fieldName: String) {
        viewModelScope.launch {
            db.collection("soccer_fields")
                .whereEqualTo("name", fieldName) // חיפוש לפי השדה "name"
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0] // נקח את המסמך הראשון שמתאים
                        val details = FieldDetails(
                            name = document.getString("name") ?: ""
                        )
                        _fieldDetails.value = details
                        Log.d("FieldDetailsViewModel", "🔍 נמצא מגרש: ${details.name}")
                    } else {
                        Log.e("FieldDetailsViewModel", "⚠️ לא נמצא מגרש בשם: $fieldName")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FieldDetailsViewModel", "❌ שגיאה בשליפת המגרש: ", e)
                }
        }
    }

}