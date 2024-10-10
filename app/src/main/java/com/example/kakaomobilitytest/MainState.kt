package com.example.kakaomobilitytest

import com.airbnb.mvrx.MavericksState
import com.example.kakaomobilitytest.api.Location
import com.example.kakaomobilitytest.api.RouteState

data class MainState(
    val locations: List<Location> = emptyList(), // 기본 값은 빈 리스트
    val errorMessage: String? = null, // 에러 메시지
    val errorCode: Int? = null, // 에러 코드
    val selectedOrigin: String? = null, // 선택된 출발지
    val selectedDestination: String? = null, // 선택된 도착지
    val shouldNavigateToMap: Boolean = false, // 경로 조회 후 MapActivity로 이동 여부
    // 경로에 대한 정보 (각 구간별 위도, 경도와 교통 상태를 포함한 리스트)
    val routeStates: List<RouteState> = emptyList() // RouteState 리스트 (startLng, startLat, endLng, endLat, state)
) : MavericksState
