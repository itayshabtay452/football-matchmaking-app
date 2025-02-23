package com.example.soccergamesfinder.data
import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationRepository(private val context: Context) {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        return try {
            val location = fusedLocationProviderClient.lastLocation.await()
            location?.let { it.latitude to it.longitude }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
