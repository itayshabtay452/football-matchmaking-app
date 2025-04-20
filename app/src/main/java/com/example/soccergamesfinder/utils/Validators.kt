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

    fun validateNotBlank(value: String, errorMessage: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult(false, errorMessage)
        } else {
            ValidationResult(true)
        }
    }
}