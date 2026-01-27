package com.example.weatherapppp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherapppp.data.local.PreferencesManager
import com.example.weatherapppp.viewmodel.WeatherUiState
import com.example.weatherapppp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }

    var city by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(prefsManager.temperatureUnit) }

    LaunchedEffect(unit) {
        prefsManager.temperatureUnit = unit
        if (city.isNotBlank()) {
            viewModel.loadWeather(city, unit)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
// Switch C/F
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Unit: ${if (unit == "celsius") "°C" else "°F"}")
            Switch(
                checked = unit == "fahrenheit",
                onCheckedChange = { unit = if (it) "fahrenheit" else "celsius" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.loadWeather(city, unit) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = viewModel.uiState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is WeatherUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is WeatherUiState.Success -> {
                WeatherContent(
                    city = city,
                    weather = state.weather,
                    isOffline = state.isOffline
                )
            }
        }
    }
}