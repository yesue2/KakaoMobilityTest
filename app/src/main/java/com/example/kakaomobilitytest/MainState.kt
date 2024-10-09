package com.example.kakaomobilitytest

import com.airbnb.mvrx.MavericksState
import com.example.kakaomobilitytest.api.Location

data class MainState(
    val locations: List<Location> = emptyList(),
    val error: String? = null
) : MavericksState
