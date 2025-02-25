package com.example.soccergamesfinder.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class GameRepository {
    private val firestore = Firebase.firestore
    private val gamesCollection = firestore.collection("games")

    suspend fun createGame(game: Game): Boolean {
        return try {
            gamesCollection.add(game).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isGameSlotTaken(fieldId: String, date: String, timeRange: String): Boolean {
        return try {
            val querySnapshot = gamesCollection
                .whereEqualTo("fieldId", fieldId)
                .whereEqualTo("date", date)
                .whereEqualTo("timeRange", timeRange)
                .get()
                .await()

            !querySnapshot.isEmpty // אם יש תוצאה - המשבצת כבר תפוסה
        } catch (e: Exception) {
            false // במקרה של שגיאה נניח שהמשבצת לא תפוסה
        }
    }

    suspend fun hasUserGameOnDate(userId: String, date: String): Boolean {
        return try {
            val querySnapshot = gamesCollection
                .whereEqualTo("createdByUserId", userId)
                .whereEqualTo("date", date)
                .get()
                .await()

            !querySnapshot.isEmpty // אם יש תוצאה - למשתמש כבר יש משחק באותו יום
        } catch (e: Exception) {
            false
        }
    }


}
