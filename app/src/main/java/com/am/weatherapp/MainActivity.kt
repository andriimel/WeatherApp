package com.am.weatherapp


import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

import androidx.lifecycle.lifecycleScope
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.location.LocationPermissionHandler

import com.am.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var permissionHandler: LocationPermissionHandler
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionHandler.handlePermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        permissionHandler = LocationPermissionHandler(
            activity = this,
            permissionLauncher = permissionLauncher
        ) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                if (!permissionHandler.shouldShowRationale()) {
                    // Користувач відмовив і натиснув "більше не питати"
                    permissionHandler.openAppSettings()
                } else {
                    Toast.makeText(this, "Потрібен дозвіл для визначення локації", Toast.LENGTH_SHORT).show()
                }
            }
        }
        checkLocationPermission()
        val apiKey = "c50335248c6142ed803172103251104"
        val city = "London"

        lifecycleScope.launch {
            try {
                val  response = RetrofitInstance.api.getCurrentWeather(apiKey, city)
                Log.d("Weather", "City: ${response.location.name}")
                Log.d("Weather", "Temp: ${response.current.temp_c}°C")
                Log.d("Weather", "Curr: ${response.current.condition.text}")
            } catch (e: Exception) {
                Log.e("Weather", "Error: ${e.message}")
            }
        }
        setContent {
            WeatherAppTheme {

            }
        }
    }

    private fun checkLocationPermission() {
        if (permissionHandler.hasPermission()) {
            getLocation()
        } else {
            permissionHandler.requestPermission()
        }
    }

    private fun getLocation() {
        // Отримання геолокації
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Toast.makeText(this, "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()

                    // ТУТ МОЖНА ВИКЛИКАТИ API ПОГОДИ, передаючи lat/lon
                } else {
                    Toast.makeText(this, "Локація не знайдена", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Помилка локації: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
