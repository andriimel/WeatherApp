package com.am.weatherapp.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.weatherapp.Utilities.Constants
import com.am.weatherapp.api.HourlyWeatherItem
import com.am.weatherapp.api.NetworkResponse
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.api.WeatherState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil

class WeatherViewModel: ViewModel() {
    private val _weather = MutableStateFlow<NetworkResponse<WeatherState>>(NetworkResponse.Loading)
    val weather: StateFlow<NetworkResponse<WeatherState>> = _weather

    fun loadWeatherForLocation(location: String, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _weather.value = NetworkResponse.Loading
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    location = location,
                    apiKey = Constants.apiKey
                )

                val cityName = response.location.name
                val hourlyForecast = response.forecast.forecastday[0].hour.map {
                    HourlyWeatherItem(
                        time = it.time,
                        tempCelsius = it.temp_c.toString(),
                        iconUrl = "https:${it.condition.icon}"
                    )
                }
                val roundTemp = ceil((response.current.temp_c).toDouble()).toInt()
                _weather.value = NetworkResponse.Success(
                WeatherState(
                    cityName = cityName,
                    temperature = "${roundTemp}°C",
                    description = response.current.condition.text,
                    iconUrl = "https:${response.current.condition.icon}",
                    hourlyData = hourlyForecast
                    )
                    )

                onSuccess(cityName)
            }catch (e: Exception) {
                _weather.value = NetworkResponse.Error("Failed to load weather")
            }
        }
    }
}
