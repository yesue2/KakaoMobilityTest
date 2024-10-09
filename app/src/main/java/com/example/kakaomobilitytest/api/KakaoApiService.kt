package com.example.kakaomobilitytest.api

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("coding-assignment/locations")
    suspend fun getLocations(): LocationResponse

//    @GET("coding-assignment/routes")
//    suspend fun getRoutes(
//        @Query("origin") origin: String,
//        @Query("destination") destination: String
//    ): List<RouteResponse>
//
//    @GET("coding-assignment/distance-time")
//    suspend fun getDistanceTime(
//        @Query("origin") origin: String,
//        @Query("destination") destination: String
//    ): DistanceTimeResponse
}

