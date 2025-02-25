// Field.kt
package com.example.soccergamesfinder.data

import com.google.firebase.firestore.DocumentSnapshot

data class Field(
    val id: String = "",
    val name: String = "",
    val fieldSize: String = "",
    val fieldType: String = "",
    val hasLighting: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val paid: Boolean = false,
    val requiresOwnerApproval: Boolean = false,
    val distanceFromUser: Double? = null
)

fun DocumentSnapshot.toField(): Field {
    return Field(
        id = id,
        name = getString("name") ?: "",
        fieldSize = getString("fieldSize") ?: "",
        fieldType = getString("fieldType") ?: "",
        hasLighting = getBoolean("hasLighting") ?: false,
        latitude = getDouble("latitude") ?: 0.0,
        longitude = getDouble("longitude") ?: 0.0,
        paid = getBoolean("paid") ?: false,
        requiresOwnerApproval = getBoolean("requiresOwnerApproval") ?: false
    )
}
