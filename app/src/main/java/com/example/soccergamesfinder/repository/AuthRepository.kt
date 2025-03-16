package com.example.soccergamesfinder.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
        googleSignInClient.signOut()

    }

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun signInWithGoogle(data: Intent?): FirebaseUser? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try{
            val account: GoogleSignInAccount? = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }
}