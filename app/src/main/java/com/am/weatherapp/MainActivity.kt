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
import com.am.weatherapp.location.LastLocationManager
import com.am.weatherapp.location.LocationHandler

class MainActivity : ComponentActivity() {
    private lateinit var permissionHandler: LocationPermissionHandler
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private lateinit var locationHandler: LocationHandler
    private lateinit var lastLocationManager: LastLocationManager


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionHandler.handlePermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationHandler = LocationHandler(this)
        lastLocationManager = LastLocationManager(this)


        val lastCity = lastLocationManager.getLastCity()

        permissionHandler = LocationPermissionHandler(
            activity = this,
            permissionLauncher = permissionLauncher
        ) { isGranted ->
            if (isGranted) {
                locationHandler.getCityFromCurrentLocation { city ->
                    if (city != null) {
//                        if (lastCity != null) {
//                            viewModel.loadWeatherForLocation(lastCity)
//                        } else {
//                            viewModel.loadWeatherForLocation(city)
//                        }
                        viewModel.loadWeatherForLocation(city)
                    } else {
                        Toast.makeText(
                            this,
                            "Could not detect city from location", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }




        setContent {
            WeatherAppTheme {

                val weatherState by viewModel.weather.collectAsState()

                WeatherPage(
                    weatherState = weatherState,
                    onRequestLocation = { permissionHandler.checkAndRequestPermission() },
                    onSearchCity = { city ->
                        viewModel.loadWeatherForLocation(city)
                    lastLocationManager.saveLastCity(city)}
                )
            }
        }

        permissionHandler.checkAndRequestPermission()
    }
}
