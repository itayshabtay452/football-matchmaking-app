package com.example.soccergamesfinder.utils

import com.example.soccergamesfinder.model.Field

object FieldFilterManager {

    /**
     * Filters the given list of fields based on the provided criteria.
     */
    fun filterFields(
        allFields: List<Field>,
        nameQuery: String = "",
        cityQuery: String = "",
        selectedSize: String? = null,
        lightingOnly: Boolean = false,
        maxDistance: Double? = null,
        minGameCount: Int? = null
    ): List<Field> {
        return allFields.filter { field ->
            val matchesName = nameQuery.isBlank() || field.name.contains(nameQuery, ignoreCase = true)
            val matchesCity = cityQuery.isBlank() || field.address.contains(cityQuery, ignoreCase = true)
            val matchesSize = selectedSize == null || field.size == selectedSize
            val matchesLighting = !lightingOnly || field.lighting
            val matchesDistance = maxDistance == null || (field.distance ?: Double.MAX_VALUE) <= maxDistance
            val matchesGameCount = minGameCount == null || (field.games.size >= minGameCount)

            matchesName && matchesCity && matchesSize && matchesLighting && matchesDistance && matchesGameCount
        }
    }

    /**
     * Sorts the given list of fields based on the selected sort option.
     */
    fun sortFields(
        fields: List<Field>,
        sortBy: SortOption = SortOption.BY_DISTANCE
    ): List<Field> {
        return when (sortBy) {
            SortOption.BY_DISTANCE -> fields.sortedBy { it.distance ?: Double.MAX_VALUE }
            SortOption.BY_NAME -> fields.sortedBy { it.name }
            SortOption.BY_RATING -> fields.sortedByDescending { it.rating ?: 0.0 }
            SortOption.BY_GAME_COUNT -> fields.sortedByDescending { it.games.size }
        }
    }

    enum class SortOption {
        BY_DISTANCE,
        BY_NAME,
        BY_RATING,
        BY_GAME_COUNT
    }
}
