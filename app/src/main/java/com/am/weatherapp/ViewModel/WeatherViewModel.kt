package com.am.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.api.WeatherState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val _weather = MutableStateFlow(WeatherState())
    val weather : StateFlow<WeatherState> = _weather

    fun loadWeatherForLoacation(lat : Double, lon : Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    location = "$lat,$lon",
                    apiKey = "c50335248c6142ed803172103251104"
                )
                _weather.value = WeatherState(
                    cityName = response.location.name,
                    temperature = "${response.current.temp_c}°C",
                    description = response.current.condition.text,
                    iconUrl = "https:${response.current.condition.icon}")
            }catch (e: Exception) {
                Log.e("Weather", "Error: ${e.message}")
            }
        }
    }
}