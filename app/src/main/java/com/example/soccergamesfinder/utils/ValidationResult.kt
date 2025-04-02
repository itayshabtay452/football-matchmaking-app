package com.example.soccergamesfinder.utils

/**
 * Represents the result of a validation check.
 */

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
