package com.example.soccergamesfinder.ui.screens.home

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.io.File

fun addDummyUsersToFirestore(context: Context) {
    FirebaseFirestore.getInstance().collection("users")
        .get()
        .addOnSuccessListener { result ->
            val userMap = result.documents.associate { doc ->
                val id = doc.id
                val nickname = doc.getString("nickname") ?: "Unknown"
                id to nickname
            }

            val json = Gson().toJson(userMap)

            try {
                val file = File(context.getExternalFilesDir(null), "user_nicknames.json")
                file.writeText(json)
                Log.d("DemoData", "✔️ Saved to: ${file.absolutePath}")
            } catch (e: Exception) {
                Log.e("DemoData", "❌ Failed to save user nicknames", e)
            }
        }
        .addOnFailureListener {
            Log.e("DemoData", "❌ Error loading users", it)
        }
}