package com.example.soccergamesfinder.data

data class ProfileFormState(
    val firstName: String = "",
    val lastName: String = "",
    val selectedAge: String = "",
    val nickName: String = "",
    val location: String = "",
    val imageUri: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val ageError: String? = null,
    val nickNameError: String? = null,
    val locationError: String? = null,
    val imageUriError: String? = null
)