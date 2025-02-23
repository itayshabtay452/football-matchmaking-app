package com.example.soccergamesfinder.ui.utils

import kotlin.math.*

object LocationUtils {
    private const val EARTH_RADIUS = 6371.0 // רדיוס כדור הארץ בק"מ

    fun calculateDistance(
        startLat: Double, startLng: Double,
        endLat: Double, endLng: Double
    ): Double {
        val dLat = Math.toRadians(endLat - startLat)
        val dLng = Math.toRadians(endLng - startLng)

        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(startLat)) *
                cos(Math.toRadians(endLat)) *
                sin(dLng / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c // מחזיר מרחק בקילומטרים
    }
}
