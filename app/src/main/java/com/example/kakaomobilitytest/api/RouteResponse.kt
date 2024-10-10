package com.example.kakaomobilitytest.api

import android.util.Log
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

// 실제 API 호출 함수에서 응답을 처리
suspend fun getRoutesResponse(origin: String, destination: String): ApiResponse {
    try {
        // API 호출
        val response = ApiClient.apiService.getRoutes(origin, destination)

        // 응답 데이터에 따라 처리
        return if (response is List<*>) {
            val routeResponses = response.filterIsInstance<RouteResponse>() // 경로가 있는 정상 응답
            RouteSuccessResponse(routeResponses)
        } else {
            // 경로가 없을 때의 에러 응답
            val errorResponse = response as Map<String, Any>
            val code = errorResponse["code"] as? Int ?: 0
            val message = errorResponse["message"] as? String ?: "Unknown error"
            ErrorResponse(code, message)
        }
    } catch (e: Exception) {
        Log.e("API Error", e.message ?: "Unknown error")
        return ErrorResponse(500, e.message ?: "Unknown error")
    }
}

// RouteResponse 처리 로직
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