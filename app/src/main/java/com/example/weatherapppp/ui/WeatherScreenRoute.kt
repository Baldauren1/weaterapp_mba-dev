package com.example.weatherapppp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapppp.data.api.RetrofitClient
import com.example.weatherapppp.data.local.WeatherCache
import com.example.weatherapppp.data.repository.WeatherRepository
import com.example.weatherapppp.viewmodel.WeatherViewModel
import com.example.weatherapppp.viewmodel.WeatherViewModelFactory

@Composable
fun WeatherScreenRoute(
    city: String,
    unit: String = "celsius"
) {
    val context = LocalContext.current

    val repo = WeatherRepository(
        weatherApi = RetrofitClient.weatherApi,
        geoApi = RetrofitClient.geoApi,
        cache = WeatherCache(context)
    )

    val vm: WeatherViewModel = viewModel(factory = WeatherViewModelFactory(repo))

    LaunchedEffect(city, unit) {
        if (city.isNotBlank()) vm.loadWeather(city.trim(), unit)
    }

    WeatherScreen(viewModel = vm)
}
