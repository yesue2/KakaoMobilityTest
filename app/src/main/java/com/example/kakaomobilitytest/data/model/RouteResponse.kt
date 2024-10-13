package com.example.kakaomobilitytest.data.model

import com.google.gson.annotations.SerializedName

data class RouteResponse(
    val points: String,
    @SerializedName("traffic_state")
    val trafficState: String
)

sealed class ApiResponse

data class RouteSuccessResponse(
    val routes: List<RouteResponse>
) : ApiResponse()

data class ErrorResponse(
    val code: Int,
    val message: String
) : ApiResponse()


data class RouteState(
    val startLng: Double,
    val startLat: Double,
    val endLng: Double,
    val endLat: Double,
    val state: String
)