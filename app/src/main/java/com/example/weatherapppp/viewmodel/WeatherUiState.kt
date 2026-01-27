package com.example.weatherapppp.viewmodel

import com.example.weatherapppp.data.model.WeatherResponse

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(
        val weather: WeatherResponse,
        val isOffline: Boolean
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
