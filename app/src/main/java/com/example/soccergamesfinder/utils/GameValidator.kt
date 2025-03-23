package com.example.soccergamesfinder.utils

import android.annotation.SuppressLint
import com.example.soccergamesfinder.data.Game
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object GameValidator {
    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // ✅ ממיר תאריך ושעה ל-Timestamp ובודק שהם בעתיד
    fun convertToTimestamp(date: String, time: String): ValidationResult {
        if (date.isBlank() || time.isBlank())
            return ValidationResult.Error("📅 יש לבחור תאריך ושעה")

        return try {
            val parsedDate = dateFormat.parse("$date $time")
            if (parsedDate == null || parsedDate.before(Date())) {
                ValidationResult.Error("🚫 התאריך והשעה חייבים להיות בעתיד")
            } else {
                ValidationResult.SuccessWithData(Timestamp(parsedDate))
            }
        } catch (e: Exception) {
            ValidationResult.Error("❌ פורמט לא תקין")
        }
    }

    // ✅ מוודא שמועד הסיום אחרי מועד ההתחלה
    fun validateGameTimes(startTimestamp: Timestamp, endTimestamp: Timestamp): ValidationResult {
        return if (endTimestamp > startTimestamp) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("⏳ מועד הסיום חייב להיות אחרי מועד ההתחלה")
        }
    }

    fun validateGameSlot(newGame: Game, existingGamesInField: List<Game>,
                                 userGames: List<Game>): ValidationResult {

        val isFieldBusy = existingGamesInField.any {
            it.startTime < newGame.endTime && it.endTime > newGame.startTime
        }
        if (isFieldBusy) {
            return ValidationResult.Error("המגרש תפוס בשעות שנבחרו")
        }

        val userBusy = userGames.any {
            it.startTime < newGame.endTime && it.endTime > newGame.startTime
        }
        if (userBusy) {
            return ValidationResult.Error("אתה משתתף או יצרת משחק אחר בשעות הללו")
        }

        return ValidationResult.Success
    }

    fun validateJoinGame(game: Game, userId: String, userGames: List<Game>): ValidationResult{

        if (userId in game.players) {
            return ValidationResult.Error("אתה כבר רשום")
        }

        if (game.isGameFull()) {
            return ValidationResult.Error("המגרש מלא")
        }

        val hasConflict = userGames.any {
            it.startTime < game.endTime && it.endTime > game.startTime
        }

        if (hasConflict) {
            return ValidationResult.Error("אתה משתתף או יצרת משחק אחר בשעות הללו")
        }

        return ValidationResult.Success

    }


}
