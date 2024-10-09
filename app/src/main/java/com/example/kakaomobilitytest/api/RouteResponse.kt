package com.example.kakaomobilitytest.api

data class RouteResponse(
    val points: String,          // 각 포인트의 경도와 위도가 콤마로 구분된 문자열
    val traffic_state: String    // 도로 상태 (e.g., "UNKNOWN", "JAM", "DELAY", "SLOW", "NORMAL", "BLOCK")
)
