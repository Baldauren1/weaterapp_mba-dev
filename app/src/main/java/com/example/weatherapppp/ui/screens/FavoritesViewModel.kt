package com.example.weatherapppp.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppp.data.api.RetrofitClient
import com.example.weatherapppp.data.auth.AuthRepository
import com.example.weatherapppp.data.favorites.FavoriteCity
import com.example.weatherapppp.data.favorites.FavoritesRepository
import com.example.weatherapppp.data.local.WeatherCache
import com.example.weatherapppp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val uid: String = "",
    val items: List<FavoriteCity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val weatherById: Map<String, FavoriteWeatherState> = emptyMap()
)

class FavoritesViewModel(
    application: Application,
    private val authRepo: AuthRepository = AuthRepository(),
    private val favRepo: FavoritesRepository = FavoritesRepository()
) : AndroidViewModel(application) {

    private val weatherRepo: WeatherRepository by lazy {
        WeatherRepository(
            weatherApi = RetrofitClient.weatherApi,
            geoApi = RetrofitClient.geoApi,
            cache = WeatherCache(getApplication())
        )
    }

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    private var unitForFavorites: String = "celsius"

    fun start() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                val uid = authRepo.ensureSignedIn()
                _state.update { it.copy(uid = uid) }

                favRepo.observeFavorites(uid)
                    .catch { e ->
                        _state.update { it.copy(error = e.message, isLoading = false) }
                    }
                    .collect { list ->
                        _state.update { it.copy(items = list, isLoading = false) }
                        loadWeatherForFavorites(list)
                    }

            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun add(city: String, note: String) {
        val uid = state.value.uid
        if (uid.isBlank()) return

        val c = city.trim()
        if (c.isBlank()) {
            _state.update { it.copy(error = "City cannot be empty") }
            return
        }

        viewModelScope.launch {
            runCatching { favRepo.addFavorite(uid, c, note) }
                .onFailure { _state.update { s -> s.copy(error = it.message) } }
        }
    }

    fun update(id: String, city: String, note: String) {
        val uid = state.value.uid
        if (uid.isBlank()) return

        val c = city.trim()
        if (c.isBlank()) {
            _state.update { it.copy(error = "City cannot be empty") }
            return
        }

        viewModelScope.launch {
            runCatching { favRepo.updateFavorite(uid, id, c, note) }
                .onFailure { _state.update { s -> s.copy(error = it.message) } }
        }

        _state.update { s ->
            val m = s.weatherById.toMutableMap()
            m.remove(id)
            s.copy(weatherById = m)
        }
    }

    fun delete(id: String) {
        val uid = state.value.uid
        if (uid.isBlank()) return

        viewModelScope.launch {
            runCatching { favRepo.deleteFavorite(uid, id) }
                .onFailure { _state.update { s -> s.copy(error = it.message) } }
        }

        _state.update { s ->
            val m = s.weatherById.toMutableMap()
            m.remove(id)
            s.copy(weatherById = m)
        }
    }

    fun refreshWeather() {
        _state.update { it.copy(weatherById = emptyMap()) }
        loadWeatherForFavorites(state.value.items)
    }

    private fun loadWeatherForFavorites(items: List<FavoriteCity>) {
        val existing = state.value.weatherById.toMutableMap()
        items.forEach { fav ->
            if (existing[fav.id] == null) {
                existing[fav.id] = FavoriteWeatherState.Loading
            }
        }
        _state.update { it.copy(weatherById = existing.toMap()) }

        items.forEach { fav ->
            val already = state.value.weatherById[fav.id]
            if (already is FavoriteWeatherState.Success) return@forEach

            viewModelScope.launch {
                _state.update { s ->
                    val m = s.weatherById.toMutableMap()
                    m[fav.id] = FavoriteWeatherState.Loading
                    s.copy(weatherById = m)
                }

                val result = weatherRepo.getWeather(fav.city, unitForFavorites)

                result.fold(
                    onSuccess = { (weatherResponse, isOffline) ->
                        val tempText = formatTemp(weatherResponse.current.temperature_2m, unitForFavorites)
                        val desc = formatDesc(weatherResponse.current.weather_code, isOffline)

                        _state.update { s ->
                            val m = s.weatherById.toMutableMap()
                            m[fav.id] = FavoriteWeatherState.Success(tempText, desc)
                            s.copy(weatherById = m)
                        }
                    },
                    onFailure = { e ->
                        _state.update { s ->
                            val m = s.weatherById.toMutableMap()
                            m[fav.id] = FavoriteWeatherState.Error(e.message ?: "Weather error")
                            s.copy(weatherById = m)
                        }
                    }
                )
            }
        }
    }

    private fun formatTemp(value: Double, unit: String): String {
        // Open-Meteo if unit="fahrenheit" already returns F, so just format the sign
        val symbol = if (unit == "fahrenheit") "°F" else "°C"
        return "${value.toInt()}$symbol"
    }

    private fun formatDesc(code: Int, isOffline: Boolean): String {
        val base = when (code) {
            0 -> "Clear"
            1, 2, 3 -> "Partly cloudy"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            61, 63, 65 -> "Rain"
            71, 73, 75 -> "Snow"
            80, 81, 82 -> "Rain showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm (hail)"
            else -> "Code $code"
        }
        return if (isOffline) "$base (offline)" else base
    }
}
