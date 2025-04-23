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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.am.weatherapp.api.HourlyWeatherItem
import com.am.weatherapp.ui.theme.DarkBlue
import com.am.weatherapp.ui.theme.LightBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.ceil


val currentTime: String
    get() {
        val sdf = SimpleDateFormat("h a", Locale.US)  // Format: "4 AM"
        return sdf.format(Calendar.getInstance().time)
    }

@Composable
fun HourlyWeatherCell(item: HourlyWeatherItem, isCurrentTime: Boolean){
    Box(
        modifier = Modifier
            .width(90.dp)
            .padding(vertical = 20.dp)
            .background(
                if (isCurrentTime) DarkBlue else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, color = DarkBlue, shape = RoundedCornerShape(16.dp))
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
                color = if (isCurrentTime) Color.White else DarkBlue
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
                color = if (isCurrentTime) Color.White else DarkBlue
            )
        }
    }
}


@Composable

fun HourlyForecastSection(hourlyData: List<HourlyWeatherItem>) {
    val currentTimeFormatted = remember { getCurrentFormattedTime() }


    val currentTimeIndex = hourlyData.indexOfFirst {
        formatTime(it.time) == currentTimeFormatted
    }


    val scrollState = rememberLazyListState()

    LaunchedEffect(currentTimeIndex) {
        if (currentTimeIndex != -1) {
            scrollState.animateScrollToItem(currentTimeIndex)
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        state = scrollState, // added scrollstate
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(hourlyData) { index, item ->
            val isCurrentTime = formatTime(item.time) == currentTimeFormatted
            HourlyWeatherCell(item = item, isCurrentTime = isCurrentTime)
        }
    }
}

fun getCurrentFormattedTime(): String {
    val sdf = SimpleDateFormat("h a", Locale.US)
    return sdf.format(Calendar.getInstance().time)
}

fun formatTime(timeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h a", Locale.getDefault())

    val date = inputFormat.parse(timeString) ?: return timeString
    return outputFormat.format(date)
}