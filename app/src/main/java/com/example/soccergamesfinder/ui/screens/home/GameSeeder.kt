package com.example.soccergamesfinder.util

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.UUID
import kotlin.random.Random

object GameSeeder {

    fun generateDummyGames(context: Context, amount: Int = 100) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = FirebaseFirestore.getInstance()

            // 1. טען את רשימת המשתמשים עם כינויים
            val userInput = context.assets.open("user_nicknames.json")
            val userMap: Map<String, String> = Gson().fromJson(InputStreamReader(userInput), Map::class.java) as Map<String, String>
            val userIds = userMap.keys.toList()

            // 2. טען את רשימת המגרשים
            val fieldInput = context.assets.open("facilities_dump.json")
            val fieldList: List<Map<String, Any>> = Gson().fromJson(InputStreamReader(fieldInput), List::class.java) as List<Map<String, Any>>

            repeat(amount) { index ->
                val field = fieldList.random()
                val fieldId = field["id"] as String
                val fieldName = field["name"] as String
                val fieldAddress = field["address"] as String

                val creatorId = userIds.random()
                val creatorName = userMap[creatorId] ?: "Unknown"

                val allPlayers = userIds.shuffled().take(Random.nextInt(2, 8)).toMutableSet()
                allPlayers.add(creatorId)

                val startTime = Timestamp.now().toDate().let { date ->
                    val futureDate = date.time + Random.nextLong(1, 14) * 24 * 60 * 60 * 1000  // עד 14 ימים קדימה
                    Timestamp(futureDate / 1000, 0)
                }
                val endTime = Timestamp(startTime.seconds + 2 * 60 * 60, 0) // שעתיים לאחר התחלה

                val gameId = UUID.randomUUID().toString()

                val gameData = mapOf(
                    "id" to gameId,
                    "creatorId" to creatorId,
                    "creatorName" to creatorName,
                    "description" to "",
                    "endTime" to endTime,
                    "fieldAddress" to fieldAddress,
                    "fieldId" to fieldId,
                    "fieldName" to fieldName,
                    "joinedPlayers" to allPlayers.toList(),
                    "maxPlayers" to 14,
                    "players" to null,
                    "rating" to null,
                    "startTime" to startTime,
                    "status" to "OPEN"
                )

                db.collection("games").document(gameId).set(gameData)
                    .addOnSuccessListener {
                        Log.d("Seeder", "✅ Game $index added: $gameId")

                        // עדכן את המגרש עם מזהה המשחק
                        db.collection("facilities").document(fieldId)
                            .update("games", com.google.firebase.firestore.FieldValue.arrayUnion(gameId))

                        // עדכן כל משתמש
                        allPlayers.forEach { userId ->
                            db.collection("users").document(userId)
                                .update("gamesFollowed", com.google.firebase.firestore.FieldValue.arrayUnion(gameId))
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Seeder", "❌ Failed to add game $gameId", it)
                    }
            }
        }
    }
}
