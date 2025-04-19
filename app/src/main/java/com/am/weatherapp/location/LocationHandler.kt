package com.am.weatherapp.location

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

class LocationHandler(private val context: Context){
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation(onResult: (Location?) -> Unit){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location -> onResult(location) }
            .addOnFailureListener { onResult(null)}
            }

}