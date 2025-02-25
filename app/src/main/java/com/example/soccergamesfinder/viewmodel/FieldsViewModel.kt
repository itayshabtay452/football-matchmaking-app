package com.example.soccergamesfinder.viewmodel

import android.app.Application
import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.toField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FieldsViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = Firebase.firestore
    private val _allFields = mutableListOf<Field>() // רשימה פרטית לשמירת כל הנתונים
    val allFields: List<Field> get() = _allFields // גישה לקריאה בלבד לנתונים
    val filteredFields = mutableStateListOf<Field>() // הרשימה שמוצגת למשתמש

    fun loadFields(userLocation: Location?) {
        firestore.collection("fields").get()
            .addOnSuccessListener { snapshots ->
                val loadedFields = snapshots.documents.mapNotNull { it.toField() }

                val sortedFields = userLocation?.let { location ->
                    loadedFields.map { field ->
                        val fieldLocation = Location("").apply {
                            latitude = field.latitude
                            longitude = field.longitude
                        }
                        val distance = location.distanceTo(fieldLocation) / 1000.0 // המרחק בק"מ

                        println("🔹 ${field.name}: מרחק מחושב = ${"%.1f".format(distance)} ק\"מ")

                        field.copy(distanceFromUser = distance)
                    }.sortedBy { it.distanceFromUser }
                } ?: loadedFields

                _allFields.clear()
                _allFields.addAll(sortedFields)

                filteredFields.clear()
                filteredFields.addAll(sortedFields)

                println("📌 רשימת המגרשים לאחר סינון: ${filteredFields.joinToString { "${it.name} - ${it.distanceFromUser} ק\"מ" }}")
            }
    }



    fun filterFields(fieldSize: String?, fieldType: String?, hasLighting: Boolean?, paid: Boolean?) {
        val filteredList = _allFields.filter { field ->
            (fieldSize == null || field.fieldSize == fieldSize) &&
                    (fieldType == null || field.fieldType == fieldType) &&
                    (hasLighting == null || field.hasLighting == hasLighting) &&
                    (paid == null || field.paid == paid)
        }

        filteredFields.clear()
        filteredFields.addAll(filteredList)
    }

    fun clearFilters() {
        filteredFields.clear()
        filteredFields.addAll(_allFields)
    }
}