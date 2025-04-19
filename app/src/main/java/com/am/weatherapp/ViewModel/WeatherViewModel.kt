package com.am.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.weatherapp.api.NetworkResponse
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.api.WeatherState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val _weather = MutableStateFlow<NetworkResponse<WeatherState>>(NetworkResponse.Loading)
    val weather: StateFlow<NetworkResponse<WeatherState>> = _weather

    fun loadWeatherForLoacation(lat : Double, lon : Double) {
        viewModelScope.launch {
            _weather.value = NetworkResponse.Loading
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    location = "$lat,$lon",
                    apiKey = "c50335248c6142ed803172103251104"
                )
                _weather.value = NetworkResponse.Success(
                WeatherState(
                    cityName = response.location.name,
                    temperature = "${response.current.temp_c}°C",
                    description = response.current.condition.text,
                    iconUrl = "https:${response.current.condition.icon}"))
            }catch (e: Exception) {
                _weather.value = NetworkResponse.Error("Failed to load weather")
            }
        }
    }

    fun loadWeatherForCity(city: String){
        _weather.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    location = city,
                    apiKey = "c50335248c6142ed803172103251104"
                )
                _weather.value = NetworkResponse.Success(
                    WeatherState(
                    cityName = response.location.name,
                    temperature = "${response.current.temp_c}°C",
                    description = response.current.condition.text,
                    iconUrl = "https:${response.current.condition.icon}"
                ))
            } catch (e: Exception) {
                _weather.value = NetworkResponse.Error("Failed to load weather")
            }
        }
    }
}
