package com.example.weatherapppp.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapppp.data.model.WeatherResponse
import com.example.weatherapppp.util.mapWeatherCode

@Composable
fun WeatherContent(
    city: String,
    weather: WeatherResponse,
    isOffline: Boolean
) {
    Column {
        Text(
            text = city,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        //  Offline label
        if (isOffline) {
            Text(
                text = "Offline mode",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🌡 Current weather
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "3-day forecast",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        //  Forecast list
        for (i in 0..2) {
            ForecastItem(
                date = weather.daily.time[i],
                min = weather.daily.temperature_2m_min[i],
                max = weather.daily.temperature_2m_max[i],
                code = weather.daily.weather_code[i]
            )
        }
    }
}

