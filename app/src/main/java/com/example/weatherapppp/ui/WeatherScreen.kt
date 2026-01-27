package com.example.weatherapppp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapppp.viewmodel.WeatherUiState
import com.example.weatherapppp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel
) {
    var city by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("celsius") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        // Search field
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.loadWeather(city, unit)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = viewModel.uiState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator()
            }

            is WeatherUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
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
