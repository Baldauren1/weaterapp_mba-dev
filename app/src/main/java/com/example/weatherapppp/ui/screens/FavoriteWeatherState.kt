package com.example.weatherapppp.ui.screens

sealed class FavoriteWeatherState {
    data object Idle : FavoriteWeatherState()
    data object Loading : FavoriteWeatherState()
    data class Success(val temp: String, val desc: String) : FavoriteWeatherState()
    data class Error(val message: String) : FavoriteWeatherState()
}
