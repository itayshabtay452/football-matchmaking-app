package com.example.soccergamesfinder.viewmodel.field

import com.example.soccergamesfinder.model.Field

data class FieldListState(
    val fields: List<Field> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val followedFields: List<String> = emptyList()
)
