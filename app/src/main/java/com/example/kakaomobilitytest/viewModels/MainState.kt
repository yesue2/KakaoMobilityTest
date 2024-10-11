package com.example.kakaomobilitytest.viewModels

import com.airbnb.mvrx.MavericksState
import com.example.kakaomobilitytest.data.model.Location
import com.example.kakaomobilitytest.data.model.RouteState

data class MainState(
    val locations: List<Location> = emptyList(),
    val errorMessage: String? = null,
    val errorCode: Int? = null,
    val selectedOrigin: String? = null,
    val selectedDestination: String? = null,
    val isNavigateToMap: Boolean = false,
    val routeStates: List<RouteState> = emptyList(),
    val distance: Int = 0,
    val time: Int = 0
) : MavericksState
