package com.example.kakaomobilitytest.data.model

import android.util.Log
import com.example.kakaomobilitytest.data.api.ApiClient
import com.google.gson.annotations.SerializedName

// API 응답 형태를 정의한 sealed class
sealed class ApiResponse
data class RouteSuccessResponse(
    val routes: List<RouteResponse>
) : ApiResponse()

data class ErrorResponse(
    val code: Int,
    val message: String
) : ApiResponse()

// RouteResponse 데이터 클래스 정의 (Retrofit으로 받을 데이터 모델)
data class RouteResponse(
    val points: String,
    @SerializedName("traffic_state")
    val trafficState: String
)

data class RouteState(
    val startLng: Double,
    val startLat: Double,
    val endLng: Double,
    val endLat: Double,
    val state: String
)

성// 기존 getRoutesResponse 함수 개선
fun parseRouteResponse(response: List<RouteResponse>): List<RouteState> {
    return response.map { routeResponse ->
        val pointList = routeResponse.points.split(" ")

        // 두 좌표씩 짝지어서 처리
        pointList.chunked(2).map {
            val (startLng, startLat) = it[0].split(",").map { it.toDouble() }
            val (endLng, endLat) = it[1].split(",").map { it.toDouble() }

            RouteState(
                startLng = startLng,
                startLat = startLat,
                endLng = endLng,
                endLat = endLat,
                state = routeResponse.trafficState
            )
        }
    }.flatten()
}

fun processRouteResponse(routeResponses: List<RouteResponse>): List<RouteState> {
    val routeStates = mutableListOf<RouteState>()

    routeResponses.forEach { routeResponse ->
        val pointList = routeResponse.points.split(" ")

        // 두 좌표씩 짝지어서 처리
        for (i in 0 until pointList.size - 1) {
            val (startLng, startLat) = pointList[i].split(",").map { it.toDouble() }
            val (endLng, endLat) = pointList[i + 1].split(",").map { it.toDouble() }
            Log.d("TrafficState", "traffic_state: ${routeResponse.trafficState}")

            val routeState = RouteState(
                startLng = startLng,
                startLat = startLat,
                endLng = endLng,
                endLat = endLat,
                state = routeResponse.trafficState
            )
            routeStates.add(routeState)
        }
    }

    return routeStates
}