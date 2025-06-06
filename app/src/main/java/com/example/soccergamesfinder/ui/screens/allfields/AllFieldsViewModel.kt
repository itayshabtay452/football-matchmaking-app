package com.example.soccergamesfinder.ui.screens.allfields

import androidx.lifecycle.ViewModel
import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.utils.FieldFilterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllFieldsViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(AllFieldsState())
    val state: StateFlow<AllFieldsState> = _state.asStateFlow()

    fun setInitialFields(fields: List<Field>) {
            _state.update { it.copy(originalFields = fields) } // ⬅️ תוספת חשובה
            applyFiltersAndSorting()
        }


    fun onEvent(event: AllFieldsEvent) {
        when (event) {
            is AllFieldsEvent.NameChanged -> _state.update { it.copy(nameQuery = event.value) }
            is AllFieldsEvent.CityChanged -> _state.update { it.copy(cityQuery = event.value) }
            is AllFieldsEvent.SizeChanged -> _state.update { it.copy(selectedSize = event.value) }
            is AllFieldsEvent.LightingChanged -> _state.update { it.copy(lightingOnly = event.value) }
            is AllFieldsEvent.MaxDistanceChanged -> _state.update { it.copy(maxDistance = event.value) }
            is AllFieldsEvent.MinGameCountChanged -> _state.update { it.copy(minGameCount = event.value) }
            is AllFieldsEvent.SortOptionChanged -> _state.update { it.copy(sortOption = event.option) }
            is AllFieldsEvent.ClearFilters -> {
                _state.update {
                    it.copy(
                        nameQuery = "",
                        cityQuery = "",
                        selectedSize = null,
                        lightingOnly = false,
                        maxDistance = null,
                        minGameCount = null,
                        sortOption = FieldFilterManager.SortOption.BY_DISTANCE
                    )
                }
            }

            AllFieldsEvent.LoadNextPage -> TODO()
            is AllFieldsEvent.UpdateFields -> TODO()
        }

        applyFiltersAndSorting()
    }


    private fun applyFiltersAndSorting() {
        val s = _state.value

        val filtered = FieldFilterManager.filterFields(
            allFields = s.originalFields,
            nameQuery = s.nameQuery,
            cityQuery = s.cityQuery,
            selectedSize = s.selectedSize,
            lightingOnly = s.lightingOnly,
            maxDistance = s.maxDistance,
            minGameCount = s.minGameCount
        )

        val sorted = FieldFilterManager.sortFields(
            fields = filtered,
            sortBy = s.sortOption
        )

        _state.update { it.copy(filteredFields = sorted) }
    }
}
