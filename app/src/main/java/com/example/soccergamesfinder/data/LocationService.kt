package com.example.soccergamesfinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationService(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    println("📍 מיקום התקבל: ${location.latitude}, ${location.longitude}")
                    continuation.resume(location)
                } else {
                    println("⚠️ לא התקבל מיקום")
                    continuation.resume(null)
                }
            }.addOnFailureListener { exception ->
                println("❌ שגיאה בקבלת מיקום: ${exception.message}")
                continuation.resume(null)
            }
        }
    }
}
