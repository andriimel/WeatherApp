package com.am.weatherapp.api

data class WeatherState(val cityName: String = "",
                        val temperature: String = "",
                        val description: String = "",
                        val iconUrl: String = ""
)
