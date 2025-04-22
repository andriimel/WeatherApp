package com.am.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.am.weatherapp.api.HourlyWeatherItem
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil

@Composable
fun HourlyWeatherCell(item: HourlyWeatherItem){
    Box(
        modifier = Modifier
            .width(90.dp)
            .padding(vertical = 20.dp)
            .background(
                color = Color(0x1AFFFFFF), // 90%
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, color = Color(0x80000000), shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
        ) {
            // Time
            Text(
                text = formatTime(item.time),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B40)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Icon URL

            Image(
                painter = rememberAsyncImagePainter(model = item.iconUrl),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

// Temp
            val roundTemp = ceil((item.tempCelsius).toDouble()).toInt()
            Text(
                text = "$roundTemp°C",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B40)
            )
        }
    }
}


@Composable

fun HourlyForecastSection(hourlyData: List<HourlyWeatherItem>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        itemsIndexed(hourlyData) { index, item ->
            HourlyWeatherCell(item = item)
        }
    }
}

fun formatTime(timeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h a", Locale.getDefault()) // Americans format

    val date = inputFormat.parse(timeString) ?: return timeString
    return outputFormat.format(date)
}