package com.example.soccergamesfinder.ui.screens.allfields

import com.example.soccergamesfinder.utils.FieldFilterManager

sealed class AllFieldsEvent {
    data class UpdateFields(val fields: List<com.example.soccergamesfinder.model.Field>) : AllFieldsEvent()

    data class NameChanged(val value: String) : AllFieldsEvent()
    data class CityChanged(val value: String) : AllFieldsEvent()
    data class SizeChanged(val value: String?) : AllFieldsEvent()
    data class LightingChanged(val value: Boolean) : AllFieldsEvent()
    data class MaxDistanceChanged(val value: Double?) : AllFieldsEvent()
    data class MinGameCountChanged(val value: Int?) : AllFieldsEvent()

    data class SortOptionChanged(val option: FieldFilterManager.SortOption) : AllFieldsEvent()

    object ClearFilters : AllFieldsEvent()
    object LoadNextPage : AllFieldsEvent()
}
