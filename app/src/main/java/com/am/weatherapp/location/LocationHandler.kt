package com.am.weatherapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationHandler(private val context: Context){
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCityFromCurrentLocation(onCityReady: (String?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val cityName = getCityNameFromLocation(location)
                    onCityReady(cityName)
                } else {
                    onCityReady(null)
                }
            }
            .addOnFailureListener {
                Log.e("LocationHandler", "Failed to get location", it)
                onCityReady(null)
            }
    }
    private fun getCityNameFromLocation(location: Location): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.firstOrNull()?.locality
        } catch (e: Exception) {
            Log.e("LocationHandler", "Geocoder failed", e)
            null
        }
    }
}