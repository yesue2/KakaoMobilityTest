package com.example.kakaomobilitytest.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// 1. RouteResponse 데이터 클래스 정의 (Retrofit으로 받을 데이터 모델)
data class RouteResponse(
    @SerializedName("points")
    val points: String,
    @SerializedName("traffic_state")
    val trafficState: String
)

// 2. API 응답을 처리하는 함수
fun processRouteResponse(routeResponses: List<RouteResponse>) {
    // 각 응답을 처리
    routeResponses.forEach { routeResponse ->
        // points 문자열을 스페이스로 먼저 분리하여 각 경로의 좌표쌍으로 나눔
        val pointList = routeResponse.points.split(" ")

        // 경도(lng)와 위도(lat)를 저장할 리스트
        val lngList = mutableListOf<Double>()
        val latList = mutableListOf<Double>()

        // 각 좌표쌍을 순회하며 경도와 위도를 나누어 저장
        pointList.forEach { point ->
            val coordinates = point.split(",") // 콤마를 기준으로 경도와 위도를 나눔
            if (coordinates.size == 2) {
                try {
                    val lng = coordinates[0].toDouble() // 경도
                    val lat = coordinates[1].toDouble() // 위도
                    lngList.add(lng) // 경도 리스트에 저장
                    latList.add(lat) // 위도 리스트에 저장
                } catch (e: NumberFormatException) {
                    Log.e("processRouteResponse", "Invalid coordinates: $point")
                }
            }
        }

        // 결과 출력 (디버깅 용도)
        Log.d("processRouteResponse", "경도 리스트: $lngList")
        Log.d("processRouteResponse", "위도 리스트: $latList")
    }
}

// 3. 실제 JSON 데이터를 처리하는 부분 (예시)
fun main() {
    val jsonResponse = """
    [
        {
            "points": "127.26466662489628,36.50529575695577 127.26498011747837,36.50519855468101 127.2650694189438,36.50519910482981",
            "traffic_state": "UNKNOWN"
        }
    ]
    """

    // Gson을 이용해 JSON 데이터를 RouteResponse 객체로 변환
    val gson = Gson()
    val routeResponses: List<RouteResponse> = gson.fromJson(jsonResponse, Array<RouteResponse>::class.java).toList()

    // 좌표 데이터를 처리
    processRouteResponse(routeResponses)
}
