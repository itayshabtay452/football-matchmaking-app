package com.example.soccergamesfinder.viewmodel

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.soccergamesfinder.R

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth

    var user = mutableStateOf(auth.currentUser)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun register(email: String, password: String) {
        if(email.isBlank() || password.isBlank()){
            errorMessage.value = "יש למלא אימייל וסיסמה"
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) user.value = auth.currentUser
            else errorMessage.value = task.exception?.message
        }
    }

    fun login(email: String, password: String) {
        if(email.isBlank() || password.isBlank()){
            errorMessage.value = "יש למלא אימייל וסיסמה"
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) user.value = auth.currentUser
            else errorMessage.value = task.exception?.message
        }
    }

    fun getGoogleSignInIntent(): Intent {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getApplication<Application>().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(getApplication(), options).signInIntent
    }

    fun signInWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) user.value = auth.currentUser
            else errorMessage.value = task.exception?.message
        }
    }

    fun logout() {
        auth.signOut()
        user.value = null
    }
}
