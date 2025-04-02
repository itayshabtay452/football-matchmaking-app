package com.example.soccergamesfinder.services

/**
 * Interface for providing location services.
 * Abstracted for modularity and easier testing.
 */
interface LocationService {
    /**
     * Retrieves the current device location (latitude, longitude).
     * @return Result<Pair<Double, Double>> containing coordinates or failure
     */
    suspend fun getCurrentLocation(): Result<Pair<Double, Double>>
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String?
}