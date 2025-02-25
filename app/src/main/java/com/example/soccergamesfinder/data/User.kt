package com.example.soccergamesfinder.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val nickname: String = ""
)

// פונקציית עזר להמרת DocumentSnapshot ל-User
fun DocumentSnapshot.toUser(): User? = toObject(User::class.java)
