package com.example.weatherapppp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenFavorites: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Weather App (Assignment 7 base)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Text("Assignment 8: Favorites & Notes with Firebase")
        Spacer(Modifier.height(20.dp))
        Button(onClick = onOpenFavorites) {
            Text("Open Favorites")
        }
    }
}