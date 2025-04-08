package com.example.soccergamesfinder.viewmodel.field

import com.example.soccergamesfinder.data.Field

data class FieldListState(
    val fields: List<Field> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
