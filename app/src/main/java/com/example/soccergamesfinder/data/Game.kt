package com.example.soccergamesfinder.data

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Game(
    val id: String = "",
    val fieldId: String = "",       // שם המתקן
    val startTime: Timestamp = Timestamp.now(),       // גודל
    val endTime: Timestamp = Timestamp.now(),    // גידור קיים
    val creatorId: String = "",      // דוא"ל איש קשר
    val players: List<String> = emptyList(),      // טלפון איש קשר
    val maxPlayers: Int = 10,    // חניה לרכבים
    val status: String = "open",    // כתובת
    val description: String? = null    // תאורה קיימת
){
    fun getFormattedStartTime(): String = formatTimestampToString(startTime)
    fun getFormattedEndTime(): String = formatTimestampToString(endTime)
}

fun formatTimestampToString(timestamp: Timestamp?): String {
    if (timestamp == null) return "תאריך לא זמין" // ✅ אם `timestamp` ריק, הצג הודעה מתאימה
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // ✅ פורמט תאריך
    return sdf.format(timestamp.toDate()) // ✅ המרת `Timestamp` ל- `String`
}
