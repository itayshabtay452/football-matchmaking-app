package com.example.soccergamesfinder.data

data class Field(
    val id: String = "",
    val name: String = "",       // שם המתקן
    val size: String = "",       // גודל
    val fencing: String = "",    // גידור קיים
    val email: String = "",      // דוא"ל איש קשר
    val phone: String = "",      // טלפון איש קשר
    val parking: String = "",    // חניה לרכבים
    val address: String = "",    // כתובת
    val lighting: String = ""    // תאורה קיימת
)