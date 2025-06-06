package com.example.soccergamesfinder.ui.screens.allfields

import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.utils.FieldFilterManager

data class AllFieldsState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val originalFields: List<Field> = emptyList(),     // מה־FieldListViewModel
    val filteredFields: List<Field> = emptyList(),     // אחרי סינון ומיון

    // שדות סינון
    val nameQuery: String = "",
    val cityQuery: String = "",
    val selectedSize: String? = null,
    val lightingOnly: Boolean = false,
    val maxDistance: Double? = null,
    val minGameCount: Int? = null,

    // מיון
    val sortOption: FieldFilterManager.SortOption = FieldFilterManager.SortOption.BY_DISTANCE,

    // פאגינציה
    val pageSize: Int = 10,
    val currentPage: Int = 1
)
