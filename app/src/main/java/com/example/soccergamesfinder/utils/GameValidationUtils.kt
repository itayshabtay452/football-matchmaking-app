package com.example.soccergamesfinder.utils

import com.example.soccergamesfinder.data.Game
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object GameValidationUtils {
    /**
     * האם שני טווחי זמן חופפים
     */
    private fun isOverlapping(start1: Timestamp, end1: Timestamp, start2: Timestamp, end2: Timestamp): Boolean {
        return start1 < end2 && start2 < end1
    }

    /**
     * האם המגרש פנוי בטווח הזמן שנבחר
     */
    fun validateFieldAvailability(
        requestedStart: Timestamp,
        requestedEnd: Timestamp,
        existingGames: List<Game>
    ): ValidationResult {
        val overlapping = existingGames.any { game ->
            isOverlapping(requestedStart, requestedEnd, game.startTime, game.endTime)
        }

        return if (overlapping) {
            ValidationResult(false, "המגרש תפוס בשעות האלו")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * האם המשתמש פנוי בטווח הזמן שנבחר
     */
    fun validateUserAvailability(
        userId: String,
        requestedStart: Timestamp,
        requestedEnd: Timestamp,
        userGames: List<Game>
    ): ValidationResult {
        val overlapping = userGames.any { game ->
            userId in game.joinedPlayers &&
                    isOverlapping(requestedStart, requestedEnd, game.startTime, game.endTime)
        }

        return if (overlapping) {
            ValidationResult(false, "אתה כבר רשום למשחק בשעות האלו")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * המרה של תאריך ושעה ל־Timestamp
     */
    fun convertToTimestamp(date: String, time: String): Timestamp {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val parsed = formatter.parse("$date $time")
            ?: throw IllegalArgumentException("Invalid date/time")
        return Timestamp(parsed)
    }

    fun validateDate(date: String): ValidationResult {
        return if (date.isBlank()) {
            ValidationResult(false, "יש לבחור תאריך")
        } else {
            ValidationResult(true)
        }
    }

    fun validateStartTime(startTime: String): ValidationResult {
        return if (startTime.isBlank()) {
            ValidationResult(false, "יש לבחור שעת התחלה")
        } else {
            ValidationResult(true)
        }
    }

    fun validateEndTime(startTime: String, endTime: String): ValidationResult {
        return when {
            endTime.isBlank() -> ValidationResult(false, "יש לבחור שעת סיום")
            startTime >= endTime -> ValidationResult(false, "שעת הסיום חייבת להיות אחרי שעת ההתחלה")
            else -> ValidationResult(true)
        }
    }

    fun validateMaxPlayers(maxPlayers: Int): ValidationResult {
        return if (maxPlayers in 10..33) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "יש לבחור מספר שחקנים בין 10 ל־33")
        }
    }
}