package com.example.weatherapppp.util

fun mapWeatherCode(code: Int): String =
    when (code) {
        0 -> "Clear"
        1,2,3 -> "Partly cloudy"
        45,48 -> "Fog"
        51,53,55 -> "Drizzle"
        61,63,65 -> "Rain"
        71,73,75 -> "Snow"
        else -> "Unknown"
    }
