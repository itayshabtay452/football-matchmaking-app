package com.example.soccergamesfinder.utils

import android.net.Uri

object UserValidator{

    fun validateName(name: String): ValidationResult {
        val regex = Regex("^[a-zA-Zא-ת ]+$") // רק אותיות ורווחים
        return when {
            name.isBlank() -> ValidationResult.Error("השם לא יכול להיות ריק ❌")
            name.length < 2 -> ValidationResult.Error("השם חייב להכיל לפחות 2 תווים ❌")
            name.length > 50 -> ValidationResult.Error("השם לא יכול להיות ארוך מ-50 תווים ❌")
            !regex.matches(name) -> ValidationResult.Error("השם יכול להכיל רק אותיות ❌")
            else -> ValidationResult.Success
        }
    }


    fun validateNickname(nickname: String): ValidationResult {
        val regex = Regex("^[a-zA-Z0-9א-ת_]+$") // אותיות, מספרים וקו תחתון
        return when {
            nickname.isBlank() -> ValidationResult.Error("הכינוי לא יכול להיות ריק ❌")
            nickname.length < 3 -> ValidationResult.Error("הכינוי חייב להכיל לפחות 3 תווים ❌")
            nickname.length > 20 -> ValidationResult.Error("הכינוי לא יכול להיות ארוך מ-20 תווים ❌")
            !regex.matches(nickname) -> ValidationResult.Error("הכינוי יכול להכיל רק אותיות, מספרים וקו תחתון ❌")
            else -> ValidationResult.Success
        }
    }


    fun validateAge(age: Int?): ValidationResult {
        return when {
            age == null -> ValidationResult.Error("חובה להזין גיל ❌")
            age < 10 -> ValidationResult.Error("הגיל חייב להיות לפחות 10 ❌")
            age > 100 -> ValidationResult.Error("הגיל חייב להיות עד 100 ❌")
            else -> ValidationResult.Success
        }
    }


    fun validateLocation(latitude: Double?, longitude: Double?): ValidationResult {
        return if (latitude == null || longitude == null)
            ValidationResult.Error("חובה להפעיל מיקום ❌")
        else ValidationResult.Success
    }

    fun validateImageData(imageUri: Uri?, imageSize: Long?, maxSizeMB: Int = 5): ValidationResult {
        if (imageUri == null) {
            return ValidationResult.Error("יש להעלות תמונת פרופיל ❌")
        }

        // בדיקת גודל התמונה
        val maxSizeBytes = maxSizeMB * 1024 * 1024
        if (imageSize != null && imageSize > maxSizeBytes) {
            return ValidationResult.Error("התמונה חורגת מהגודל המותר (${maxSizeMB}MB) ❌")
        }

        return ValidationResult.Success
    }


}