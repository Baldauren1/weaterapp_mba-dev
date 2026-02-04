package com.example.weatherapppp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapppp.ui.WeatherScreenRoute
import com.example.weatherapppp.ui.screens.FavoritesScreen
import com.example.weatherapppp.ui.screens.HomeScreen

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(onOpenFavorites = { nav.navigate(Routes.FAVORITES) })
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onOpenWeather = { city ->
                    nav.navigate("weather/${city}")
                }
            )
        }

        composable(Routes.WEATHER) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: ""
            WeatherScreenRoute(city = city)
        }
    }
}
