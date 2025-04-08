package com.example.soccergamesfinder.services

import android.content.Intent
import androidx.activity.result.ActivityResult

/**
 * Provides abstraction for handling Google Sign-In process.
 */
interface GoogleSignInService {
    fun getSignInIntent(): Intent
    fun extractIdToken(result: ActivityResult): String?
}