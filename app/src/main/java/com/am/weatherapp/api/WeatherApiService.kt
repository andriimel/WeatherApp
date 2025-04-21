package com.am.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 1
    ) : WeatherData

}