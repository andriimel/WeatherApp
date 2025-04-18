package com.am.weatherapp.ui

import androidx.compose.runtime.Composable
import com.am.weatherapp.api.HourlyTemp

@Composable
fun TemperatureChart(data: List<HourlyTemp>){
    val points = data.mapIndexed { index, temp ->
        com.tehras.charts.line.LineChartData.Point(
            value = temp.temperature,
            label = temp.hour
        )
    }
    val chartData = com.tehras.charts.line.LineChartData(points)

    com.tehras.charts.line.LineChart(
        lineChartData = chartData,
        modifier = Modifier.fillMaxWidth(),
        animation = simpleChartAnimation(),
        pointDrawer = FilledCircularPointDrawer(),
        lineDrawer = SolidLineDrawer(color = Color.White, thickness = 4f),
        xAxisDrawer = SimpleXAxisDrawer(labelTextColor = Color.White),
        yAxisDrawer = SimpleYAxisDrawer(labelTextColor = Color.White),
    )
}