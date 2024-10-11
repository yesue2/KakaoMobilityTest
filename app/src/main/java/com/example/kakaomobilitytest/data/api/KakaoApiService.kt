package com.example.kakaomobilitytest.data.api

import com.example.kakaomobilitytest.data.model.DistanceTimeResponse
import com.example.kakaomobilitytest.data.model.LocationResponse
import com.example.kakaomobilitytest.data.model.RouteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("locations")
    suspend fun getLocations(): LocationResponse

    @GET("routes")
    suspend fun getRoutes(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): List<RouteResponse>

    @GET("distance-time")
    suspend fun getDistanceTime(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): DistanceTimeResponse
}

