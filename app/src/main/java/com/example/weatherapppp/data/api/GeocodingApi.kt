package com.example.weatherapppp.data.api

import com.example.weatherapppp.data.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 5
    ): GeocodingResponse
}
