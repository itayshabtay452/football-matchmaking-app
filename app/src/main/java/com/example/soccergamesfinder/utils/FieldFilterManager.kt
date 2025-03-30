package com.example.soccergamesfinder.utils

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.FieldFilterState

object FieldFilterManager {
    fun applyFilters(fields: List<Field>, filter: FieldFilterState): List<Field> {
        return fields.filter { field ->
            val lightingMatch = !filter.lighting || field.lighting
            val cityMatch = filter.city.isNullOrBlank() ||
                    (field.address?.contains(filter.city, ignoreCase = true) == true)
            val sizeMatch = filter.size == null || field.size == filter.size
            val distanceMatch = field.distance != null && field.distance <= filter.maxDistanceKm

            lightingMatch && cityMatch && sizeMatch && distanceMatch
        }
    }
}

