// AddFieldEvent.kt
package com.example.soccergamesfinder.ui.screens.addfield

sealed class AddFieldEvent {
    data class NameChanged(val value: String) : AddFieldEvent()
    data class AddressChanged(val value: String) : AddFieldEvent()
    data class DescriptionChanged(val value: String) : AddFieldEvent()
    data class SizeChanged(val value: String) : AddFieldEvent()
    data class LightingChanged(val value: Boolean) : AddFieldEvent()
    object FindAddressOnMap : AddFieldEvent()
    object Submit : AddFieldEvent()
    object ClearError : AddFieldEvent()
    data class ImageSelected(val uri: String) : AddFieldEvent() // future use
    data class MapMarkerMoved(val lat: Double, val lng: Double) : AddFieldEvent()
}
