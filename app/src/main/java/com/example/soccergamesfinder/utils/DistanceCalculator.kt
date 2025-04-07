package com.example.soccergamesfinder.utils

import kotlin.math.*

object DistanceCalculator {
    fun calculate(
        userLat: Double,
        userLng: Double,
        fieldLat: Double,
        fieldLng: Double
    ): Double {
        val earthRadius = 6371.0 // ק"מ
        val dLat = Math.toRadians(fieldLat - userLat)
        val dLng = Math.toRadians(fieldLng - userLng)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(fieldLat)) *
                sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
