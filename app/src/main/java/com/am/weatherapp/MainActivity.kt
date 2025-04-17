package com.am.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.am.weatherapp.api.RetrofitInstance
import com.am.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
}
