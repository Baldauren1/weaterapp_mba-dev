package com.example.weatherapppp.data.local

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    var temperatureUnit: String
        get() = prefs.getString("temperature_unit", "celsius") ?: "celsius"
        set(value) {
            prefs.edit { putString("temperature_unit", value) }
        }
}