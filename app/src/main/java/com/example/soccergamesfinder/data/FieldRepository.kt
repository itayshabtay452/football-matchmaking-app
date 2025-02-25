package com.example.soccergamesfinder.data

import android.location.Location
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FieldsRepository {
    private val firestore = Firebase.firestore

    suspend fun getFields(userLocation: Location?): List<Field> {
        return try {
            val snapshots = firestore.collection("fields").get().await()
            val loadedFields = snapshots.documents.mapNotNull { it.toField() }

            userLocation?.let { location ->
                loadedFields.map { field ->
                    val fieldLocation = Location("").apply {
                        latitude = field.latitude
                        longitude = field.longitude
                    }
                    val distance = location.distanceTo(fieldLocation) / 1000.0 // המרחק בק"מ
                    field.copy(distanceFromUser = distance)
                }.sortedBy { it.distanceFromUser }
            } ?: loadedFields
        } catch (e: Exception) {
            emptyList()
        }
    }
}
