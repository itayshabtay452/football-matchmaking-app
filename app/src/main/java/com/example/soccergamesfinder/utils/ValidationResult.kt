package com.example.soccergamesfinder.utils

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    data class SuccessWithData<T>(val data: T) : ValidationResult()
}
