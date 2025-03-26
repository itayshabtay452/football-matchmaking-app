package com.example.soccergamesfinder.utils

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.FieldFilterState

object FieldFilterManager {
    fun applyFilters(fields: List<Field>, filter: FieldFilterState): List<Field> {
        return fields.filter { field ->
            (!filter.lighting || field.lighting == "כן") &&
                    (!filter.parking || field.parking == "כן") &&
                    (!filter.fencing || field.fencing == "כן") &&
                    (filter.nameQuery.isBlank() || field.name?.contains(filter.nameQuery, true) == true) &&
                    (filter.size == null || field.size == filter.size) &&
                    (filter.maxDistanceKm == null || (field.distance != null && field.distance <= filter.maxDistanceKm))
        }
    }
}
