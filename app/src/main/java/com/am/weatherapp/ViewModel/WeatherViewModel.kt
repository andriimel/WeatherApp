package com.am.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.weatherapp.api.NetworkResponse
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.api.WeatherState
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val _weather = MutableStateFlow<NetworkResponse<WeatherState>>(NetworkResponse.Loading)
    val weather: StateFlow<NetworkResponse<WeatherState>> = _weather
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    fun loadWeatherForLocation(location: String, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _weather.value = NetworkResponse.Loading
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    location = location,
                    apiKey = "c50335248c6142ed803172103251104"
                )

                val cityName = response.location.name
                _weather.value = NetworkResponse.Success(
                WeatherState(
                    cityName = cityName,
                    temperature = "${response.current.temp_c}°C",
                    description = response.current.condition.text,
                    iconUrl = "https:${response.current.condition.icon}"))

                onSuccess(cityName)
            }catch (e: Exception) {
                _weather.value = NetworkResponse.Error("Failed to load weather")
            }
        }
    }
}
