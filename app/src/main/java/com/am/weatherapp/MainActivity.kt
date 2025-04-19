package com.am.weatherapp


import android.os.Bundle

import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider

import com.am.weatherapp.ViewModel.WeatherViewModel
import com.am.weatherapp.location.LocationPermissionHandler
import com.am.weatherapp.ui.WeatherPage

import com.am.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.compose.runtime.*
import com.am.weatherapp.location.LocationHandler

class MainActivity : ComponentActivity() {
    private lateinit var permissionHandler: LocationPermissionHandler
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private lateinit var locationHandler: LocationHandler

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) loadWeatherByLocation()
        else Toast.makeText(this, "Location permission required!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationHandler = LocationHandler(this)
        permissionHandler = LocationPermissionHandler(
            activity = this,
            permissionLauncher = permissionLauncher
        ) { isGranted ->
            if (isGranted) {
                viewModel.loadWeatherForLocation("")
            } else {
                if (!permissionHandler.shouldShowRationale()) {
                    // Refuse with "never ask again"
                    permissionHandler.openAppSettings()
                } else {
                    Toast.makeText(this, "We need access to use your location!!", Toast.LENGTH_SHORT).show()
                    //permissionHandler.openAppSettings()
                }
            }
        }


        setContent {
            WeatherAppTheme {

                val weatherState by viewModel.weather.collectAsState()

                WeatherPage(
                    weatherState = weatherState,
                    onRequestLocation = { checkLocationPermission() },
                    onSearchCity = { city -> viewModel.loadWeatherForLocation(city) }
                )
            }
        }
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (permissionHandler.hasPermission()) {
            loadWeatherByLocation()
        } else {
            permissionHandler.requestPermission()
        }
    }

    private fun loadWeatherByLocation() {
        locationHandler.getLastLocation { location ->
            if (location != null) {
                val locString = "${location.latitude},${location.longitude}"
                viewModel.loadWeatherForLocation(locString)
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
