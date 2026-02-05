package com.example.weatherapppp.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapppp.data.favorites.FavoriteCity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onOpenWeather: (String) -> Unit
) {
    val context = LocalContext.current
    val vm: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(context.applicationContext as Application)
    )

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.start() }

    var city by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { TopAppBar(title = { Text("Favorites") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {

            if (state.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Button(onClick = vm::clearError) { Text("OK") }
                Spacer(Modifier.height(12.dp))
            }

            Text("uid: ${state.uid}")
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Row {
                val isEditing = editingId != null
                Button(onClick = {
                    if (isEditing) {
                        vm.update(editingId!!, city, note)
                        editingId = null
                    } else {
                        vm.add(city, note)
                    }
                    city = ""
                    note = ""
                }) {
                    Text(if (isEditing) "Update" else "Add")
                }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(onClick = {
                    editingId = null
                    city = ""
                    note = ""
                }) { Text("Clear") }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(onClick = vm::refreshWeather) {
                Text("Refresh weather")
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(state.items) { item ->
                    FavoriteRow(
                        item = item,
                        weatherState = state.weatherById[item.id],
                        onOpenWeather = onOpenWeather,
                        onEdit = {
                            editingId = item.id
                            city = item.city
                            note = item.note
                        },
                        onDelete = { vm.delete(item.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun FavoriteRow(
    item: FavoriteCity,
    weatherState: FavoriteWeatherState?,
    onOpenWeather: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onOpenWeather(item.city) }
        ) {
            Text(item.city, style = MaterialTheme.typography.titleMedium)

            if (item.note.isNotBlank()) {
                Text(item.note, style = MaterialTheme.typography.bodyMedium)
            }

            when (val w = weatherState ?: FavoriteWeatherState.Idle) {
                FavoriteWeatherState.Idle -> {
                    Text("Weather: —", style = MaterialTheme.typography.bodySmall)
                }
                FavoriteWeatherState.Loading -> {
                    Text("Weather: loading…", style = MaterialTheme.typography.bodySmall)
                }
                is FavoriteWeatherState.Success -> {
                    Text(
                        "Weather: ${w.temp}, ${w.desc}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                is FavoriteWeatherState.Error -> {
                    Text(
                        "Weather: ${w.message}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text("createdBy: ${item.createdBy}", style = MaterialTheme.typography.bodySmall)
        }

        Row {
            TextButton(onClick = onEdit) { Text("Edit") }
            TextButton(onClick = onDelete) { Text("Delete") }
        }
    }
}
