package com.example.weatherapppp.data.repository

import android.util.Log
import com.example.weatherapppp.data.api.GeocodingApi
import com.example.weatherapppp.data.api.WeatherApi
import com.example.weatherapppp.data.local.WeatherCache
import com.example.weatherapppp.data.model.WeatherResponse

class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val geoApi: GeocodingApi,
    private val cache: WeatherCache
) {

    suspend fun getWeather(city: String, unit: String): Result<Pair<WeatherResponse, Boolean>> {
        return try {
            val geo = geoApi.searchCity(city)
            val location = geo.results?.firstOrNull() ?: return Result.failure(Exception("City not found"))

            val weather = weatherApi.getWeather(
                lat = location.latitude,
                lon = location.longitude,
                unit = unit
            )

            cache.save(weather)
            Result.success(weather to false)

        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error: ${e.message}", e)
            cache.load()?.let { (weather, _) ->
                Result.success(weather to true)
            } ?: Result.failure(Exception("No internet connection or error: ${e.message}"))
        }
    }
}