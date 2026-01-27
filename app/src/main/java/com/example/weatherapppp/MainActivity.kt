package com.example.weatherapppp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapppp.data.api.RetrofitClient
import com.example.weatherapppp.data.local.WeatherCache
import com.example.weatherapppp.data.repository.WeatherRepository
import com.example.weatherapppp.ui.WeatherScreen
import com.example.weatherapppp.ui.theme.WeatherAppppTheme
import com.example.weatherapppp.viewmodel.WeatherViewModel
import com.example.weatherapppp.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppppTheme {

                val context = LocalContext.current

                val repository = remember {
                    WeatherRepository(
                        weatherApi = RetrofitClient.weatherApi,
                        geoApi = RetrofitClient.geoApi,
                        cache = WeatherCache(context),
                    )
                }

                val viewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModelFactory(repository)
                )

                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}
