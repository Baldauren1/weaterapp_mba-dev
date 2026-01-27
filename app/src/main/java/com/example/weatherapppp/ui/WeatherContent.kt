package com.example.weatherapppp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapppp.data.model.WeatherResponse
import com.example.weatherapppp.util.mapWeatherCode
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherContent(
    city: String,
    weather: WeatherResponse,
    isOffline: Boolean,
    timestamp: Long? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = city.ifBlank { "Unknown city" },
            style = MaterialTheme.typography.titleLarge
        )

        if (isOffline) {
            val timeStr = timestamp?.let {
                SimpleDateFormat("HH:mm dd.MM", Locale.getDefault()).format(Date(it))
            } ?: "unknown"
            Text(
                text = "Offline mode - Last updated: $timeStr",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${weather.current.temperature_2m}°",
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = mapWeatherCode(weather.current.weather_code),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Feels like: ${weather.current.apparent_temperature}°")
        Text("Humidity: ${weather.current.relative_humidity_2m}%")
        Text("Wind: ${weather.current.wind_speed_10m} km/h")
        Text("Updated: ${weather.current.time}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(24.dp))

        Text("3-day forecast", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            for (i in 1..3) {
                if (i < weather.daily.time.size) {
                    ForecastItem(
                        date = weather.daily.time[i].take(10),
                        min = weather.daily.temperature_2m_min[i],
                        max = weather.daily.temperature_2m_max[i],
                        code = weather.daily.weather_code[i]
                    )
                }
            }
        }
    }
}