package com.example.soccergamesfinder.utils

import android.util.Patterns

/**
 * Contains validation functions for user input fields.
 */
object Validators {
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(false, "Invalid email address")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password cannot be empty")
            password.length < 6 -> ValidationResult(false, "Password must be at least 6 characters")
            else -> ValidationResult(true)
        }
    }

    fun validateFullName(fullName: String): ValidationResult {
        if (fullName.isBlank()) return ValidationResult(false, "Full name is required")

        val parts = fullName.trim().split("\\s+".toRegex())
        if (parts.size != 2) return ValidationResult(false, "Please enter first and last name")

        val regex = "^[A-Za-zÀ-ÿ]+$".toRegex()
        val allLetters = parts.all { part -> regex.matches(part) }
        if (!allLetters) return ValidationResult(false, "Name must contain only letters")

        return ValidationResult(true)
    }

    fun validateNicknameFormat(nickname: String): ValidationResult {
        return when {
            nickname.isBlank() -> ValidationResult(false, "Nickname is required")
            nickname.contains(" ") -> ValidationResult(false, "Nickname must be one word")
            nickname.length < 3 -> ValidationResult(false, "Nickname too short")
            else -> ValidationResult(true)
        }
    }

    fun validateLocation(latitude: Double?, longitude: Double?): ValidationResult {
        return when {
            latitude == null || longitude == null ->
                ValidationResult(false, "Location is required")
            else -> ValidationResult(true)
        }
    }

    fun validateAge(birthDate: String?): ValidationResult {
        if (birthDate.isNullOrBlank()) return ValidationResult(false, "Birth date is required")

        return try {
            val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val birth = formatter.parse(birthDate)
            val today = java.util.Calendar.getInstance()
            val dob = java.util.Calendar.getInstance().apply { time = birth }

            var age = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR)
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < dob.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--
            }

            when {
                age < 12 -> ValidationResult(false, "You must be at least 12 years old")
                age > 100 -> ValidationResult(false, "Please enter a realistic age")
                else -> ValidationResult(true)
            }
        } catch (e: Exception) {
            ValidationResult(false, "Invalid birth date format")
        }
    }


    fun validatePreferredDays(days: List<String>): ValidationResult {
        return if (days.isEmpty()) {
            ValidationResult(false, "Please select at least one preferred day")
        } else {
            ValidationResult(true)
        }
    }

    fun validateHourRange(start: Int?, end: Int?): ValidationResult {
        return when {
            start == null || end == null -> ValidationResult(false, "Please select start and end hours")
            start >= end -> ValidationResult(false, "Start hour must be before end hour")
            else -> ValidationResult(true)
        }
    }


    fun validateNotBlank(value: String, errorMessage: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult(false, errorMessage)
        } else {
            ValidationResult(true)
        }
    }
}