package com.example.soccergamesfinder.data

import com.google.firebase.firestore.PropertyName

data class Field(
    val id: String = "",

    @PropertyName("שם המתקן")
    val name: String? = null,

    @PropertyName("כתובת")
    val address: String? = null,

    @PropertyName("גודל")
    val size: String? = null,

    @PropertyName("גידור קיים")
    val fencing: String? = null,

    @PropertyName("דוא\"ל איש קשר")
    val email: String? = null,

    @PropertyName("טלפון איש קשר")
    val phone: String? = null,

    @PropertyName("חניה לרכבים")
    val parking: String? = null,

    @PropertyName("תאורה קיימת")
    val lighting: String? = null,

    @PropertyName("latitude")
    val latitude: Double? = null,

    @PropertyName("longitude")
    val longitude: Double? = null,

    val distance: Double? = null
)