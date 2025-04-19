package com.am.weatherapp.location

import android.content.Context

class LastLocationManager(context: Context) {

    private val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    fun saveLastCity(city: String){
        prefs.edit().putString("last_city",city).apply()
    }

    fun getLastCity(): String?{
        return prefs.getString("last_city",null)
    }

}

