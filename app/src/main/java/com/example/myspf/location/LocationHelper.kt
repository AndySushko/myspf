package com.example.myspf.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

data class UserLocation(
    val latitude: Double,
    val longitude: Double
)

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): UserLocation? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val location = fusedLocationClient.lastLocation.await()

    return location?.let {
        UserLocation(
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}