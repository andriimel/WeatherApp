package com.am.weatherapp.ui


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.am.weatherapp.api.WeatherState
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import com.am.weatherapp.api.HourlyWeatherItem
import com.am.weatherapp.api.NetworkResponse
import com.am.weatherapp.ui.theme.BottomBackgroundColor
import com.am.weatherapp.ui.theme.DarkBlue
import com.am.weatherapp.ui.theme.LightBlue
import com.am.weatherapp.ui.theme.LightButtonColor
import com.am.weatherapp.ui.theme.TopBackgroundColor

@Composable
fun WeatherPage(weatherState: NetworkResponse<WeatherState>,
                onRequestLocation: () -> Unit,
                onSearchCity:(String) -> Unit) {

    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val topPadding by animateDpAsState(
        targetValue = if (isSearchActive) 120.dp else 60.dp,
        label = "TopPaddingAnimation"
    )

    Box (modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit){
            detectTapGestures( onTap = {
                if (isSearchActive) {
                    isSearchActive = false
                }
            })
        }
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    TopBackgroundColor,
                    BottomBackgroundColor
                )
            )
        )
        ){
        SearchBar(isVisible = isSearchActive,
            searchText = searchText,
            onSearchTextChange = {searchText = it},
            onSearchClick = {
                onSearchCity(searchText)
                isSearchActive = false
            },
           )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(topPadding))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                Button(
                    onClick = onRequestLocation,
                    modifier = Modifier.weight(0.5f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = DarkBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Location",
                        color = DarkBlue,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Button(
                    onClick = {
                        isSearchActive = !isSearchActive
                    },
                    modifier = Modifier.weight(0.4f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightButtonColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DarkBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Search",
                        color = DarkBlue
                    )
                }
            }
            when (weatherState) {
                is NetworkResponse.Loading -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Loading...", color = Color.DarkGray)
                }
                is NetworkResponse.Success -> {
                    WeatherInfoSection(
                        cityName = weatherState.data.cityName,
                        iconUrl = weatherState.data.iconUrl,
                        temperature = weatherState.data.temperature,
                        description = weatherState.data.description,
                        hourlyWeather = weatherState.data.hourlyData
                    )
                }
                is NetworkResponse.Error -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Error: ${weatherState.message}", color = Color.Red)
                }
            }

        }
    }
}

@Composable
fun WeatherInfoSection(cityName: String,
                       iconUrl: String,
                       temperature: String,
                       description: String,
                       hourlyWeather: List<HourlyWeatherItem>
                       ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        //  City name
        Text(
            text = cityName,
            color = DarkBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Icon
        Image(
            painter = rememberAsyncImagePainter(model = iconUrl),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

            // temperature
        Text(
            text = temperature,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            modifier = Modifier.padding(top = 8.dp)
        )

        // description
        Text(
            text = description,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = DarkBlue,
            modifier = Modifier.padding(top = 4.dp)
        )
        HourlyForecastSection(hourlyData = hourlyWeather)
    }
}

