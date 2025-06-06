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
 * A concrete implementation of [LocationService] using Android's FusedLocationProviderClient
 * and Geocoder. This class handles location retrieval and reverse geocoding.
 *
 * **Note:** This class assumes that the location permission has already been granted.
 *
 * @property locationClient The fused location provider used to access location data.
 * @property context Application context used for geocoding operations.
 */
class LocationServiceImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val context: Context
) : LocationService {

    /**
     * Returns the current location of the device as latitude and longitude.
     *
     * @return A [Result] containing a [Pair] of (latitude, longitude),
     *         or a failure if location retrieval fails.
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
     * Retrieves the last known location using FusedLocationProviderClient.
     * If no location is available, the coroutine fails with an exception.
     *
     * @return A [Location] object if available, or throws an exception.
     */
    @SuppressLint("MissingPermission") // Assumes permissions are handled externally
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

    /**
     * Converts latitude and longitude into a human-readable address string.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A formatted address string, or `null` if the address could not be resolved.
     */
    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(latitude, longitude, 1)
            result?.firstOrNull()?.let { address ->
                listOfNotNull(
                    address.thoroughfare,      // Street name
                    address.subThoroughfare,   // Street number
                    address.locality,          // City
                    address.adminArea          // State/Region
                ).joinToString(", ")
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Converts a string address into geographic coordinates (latitude, longitude).
     *
     * @param address A string representing the full or partial address.
     * @return A [Result] containing the coordinates, or a failure if the address is not found.
     */
    override suspend fun getLocationFromAddress(address: String): Result<Pair<Double, Double>> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val results = geocoder.getFromLocationName(address, 1)

            if (results?.isNotEmpty() == true) {
                val location = results[0]
                Result.success(Pair(location.latitude, location.longitude))
            } else {
                Result.failure(Exception("Address not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
