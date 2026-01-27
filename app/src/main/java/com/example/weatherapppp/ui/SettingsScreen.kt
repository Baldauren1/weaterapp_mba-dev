package com.example.weatherapppp.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    context: Context,
    onUnitChanged: (String) -> Unit
) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var isCelsius by remember {
        mutableStateOf(
            prefs.getString("temperature_unit", "celsius") == "celsius"
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Temperature Unit",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(if (isCelsius) "Celsius (°C)" else "Fahrenheit (°F)")

            Switch(
                checked = isCelsius,
                onCheckedChange = {
                    isCelsius = it
                    val unit = if (it) "celsius" else "fahrenheit"

                    prefs.edit()
                        .putString("temperature_unit", unit)
                        .apply()

                    onUnitChanged(unit)
                }
            )
        }
    }
}
