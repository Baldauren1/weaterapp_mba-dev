package com.example.weatherapppp.data.local

import android.content.Context
import com.example.weatherapppp.data.model.WeatherResponse
import com.google.gson.Gson

class WeatherCache(context: Context) {

    private val prefs =
        context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun save(weather: WeatherResponse) {
        prefs.edit()
            .putString("last_weather", gson.toJson(weather))
            .putLong("timestamp", System.currentTimeMillis())
            .apply()
    }

    fun load(): WeatherResponse? {
        val json = prefs.getString("last_weather", null) ?: return null
        return gson.fromJson(json, WeatherResponse::class.java)
    }
}
