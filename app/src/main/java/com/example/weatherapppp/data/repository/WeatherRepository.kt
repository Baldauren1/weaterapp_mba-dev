package com.example.weatherapppp.data.repository

import com.example.weatherapppp.data.api.GeocodingApi
import com.example.weatherapppp.data.api.WeatherApi
import com.example.weatherapppp.data.local.WeatherCache
import com.example.weatherapppp.data.model.WeatherResponse
import java.io.IOException

class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val geoApi: GeocodingApi,
    private val cache: WeatherCache
) {

    suspend fun getWeather(
        city: String,
        unit: String
    ): Result<Pair<WeatherResponse, Boolean>> {

        return try {
            val geo = geoApi.searchCity(city)
            val location = geo.results?.firstOrNull()
                ?: return Result.failure(Exception("City not found"))

            val weather = weatherApi.getWeather(
                location.latitude,
                location.longitude,
                unit = unit
            )

            cache.save(weather)
            Result.success(weather to false)

        } catch (e: IOException) {
            cache.load()?.let {
                Result.success(it to true)
            } ?: Result.failure(Exception("No internet connection"))
        }
    }
}
