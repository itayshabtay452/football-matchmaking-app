package com.example.soccergamesfinder.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Implementation of [LocationService] using FusedLocationProviderClient.
 * Assumes location permission has already been granted before invocation.
 */
class LocationServiceImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val context: Context
) : LocationService {

    /**
     * Retrieves the current device location.
     * @return Result containing Pair<latitude, longitude> or an exception.
     */
    override suspend fun getCurrentLocation(): Result<Pair<Double, Double>> {
        return try {
            val location = getLastKnownLocation()
            Result.success(Pair(location.latitude, location.longitude))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Uses cached last known location.
     */
    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(): Location =
        suspendCancellableCoroutine { continuation ->
            locationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) continuation.resume(location)
                    else continuation.resumeWithException(Exception("Location not available"))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(latitude, longitude, 1)
            result?.firstOrNull()?.let { address ->
                listOfNotNull(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea
                ).joinToString(", ")
            }
        } catch (e: Exception) {
            null
        }
    }


}
