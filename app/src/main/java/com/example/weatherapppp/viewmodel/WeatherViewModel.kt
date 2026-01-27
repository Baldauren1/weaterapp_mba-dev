package com.example.weatherapppp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    var uiState by mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
        private set

    fun loadWeather(city: String, unit: String) {
        if (city.isBlank()) {
            uiState = WeatherUiState.Error("Please enter a city")
            return
        }

        viewModelScope.launch {
            uiState = WeatherUiState.Loading
            repository.getWeather(city, unit)
                .onSuccess {
                    uiState = WeatherUiState.Success(it.first, it.second)
                }
                .onFailure {
                    uiState = WeatherUiState.Error(it.message ?: "Error")
                }
        }
    }
}
