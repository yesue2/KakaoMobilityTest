package com.example.kakaomobilitytest

import com.airbnb.mvrx.MavericksState
import com.example.kakaomobilitytest.api.Location
import com.example.kakaomobilitytest.api.RouteResponse

data class MainState(
    val locations: List<Location> = emptyList(),
    val selectedRoute: RouteResponse? = null, // RouteResponse 타입으로 설정
    val distance: Int? = null,
    val time: Int? = null,
    val error: String? = null
) : MavericksState
