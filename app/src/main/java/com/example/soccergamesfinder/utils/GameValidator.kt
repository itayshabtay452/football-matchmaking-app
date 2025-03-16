package com.example.soccergamesfinder.utils

import android.annotation.SuppressLint
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
}
