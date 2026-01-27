package com.example.weatherapppp.data.api

import com.example.weatherapppp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,

        @Query("current")
        current: String =
            "temperature_2m,apparent_temperature,relative_humidity_2m,weather_code,wind_speed_10m",

        @Query("daily")
        daily: String =
            "temperature_2m_max,temperature_2m_min,weather_code",

        @Query("temperature_unit") unit: String,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}
