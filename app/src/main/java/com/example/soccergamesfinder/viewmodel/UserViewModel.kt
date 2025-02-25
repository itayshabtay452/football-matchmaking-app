package com.example.soccergamesfinder.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.data.toUser

class UserViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    var currentUser = mutableStateOf<User?>(null)
        private set

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get().addOnSuccessListener { snapshot ->
                currentUser.value = snapshot.toUser()
            }
        }
    }
}
