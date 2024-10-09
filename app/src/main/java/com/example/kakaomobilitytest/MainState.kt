package com.example.kakaomobilitytest

import com.airbnb.mvrx.MavericksState
import com.example.kakaomobilitytest.api.Location
import com.example.kakaomobilitytest.api.RouteResponse

data class MainState(
    val locations: List<Location> = emptyList(), // 기본 값은 빈 리스트
    val errorMessage: String? = null, // 에러 메시지
    val errorCode: Int? = null, // 에러 코드
    val selectedOrigin: String? = null, // 선택된 출발지
    val selectedDestination: String? = null // 선택된 도착지
) : MavericksState
