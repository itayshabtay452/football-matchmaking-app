package com.example.soccergamesfinder.services

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject

/**
 * Implements the GoogleSignInService using GoogleSignInClient.
 */
class GoogleSignInServiceImpl @Inject constructor(
    private val googleClient: GoogleSignInClient
) : GoogleSignInService {

    override fun getSignInIntent(): Intent = googleClient.signInIntent

    override fun extractIdToken(result: ActivityResult): String? {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            account.idToken
        } catch (e: Exception) {
            null
        }
    }
}