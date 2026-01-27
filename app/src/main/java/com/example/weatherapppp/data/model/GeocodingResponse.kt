package com.example.weatherapppp.data.model

data class GeocodingResponse(
    val results: List<Location>?
)

data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)
