package com.example.kakaomobilitytest.viewModels

import android.content.Context
import android.content.Intent
import com.airbnb.mvrx.MavericksViewModel
import com.example.kakaomobilitytest.MapActivity
import com.example.kakaomobilitytest.data.model.ErrorResponse
import com.example.kakaomobilitytest.data.model.RouteState
import com.example.kakaomobilitytest.data.model.RouteSuccessResponse
import com.example.kakaomobilitytest.data.repository.LocationRepository
import com.example.kakaomobilitytest.data.repository.processRouteResponse
import kotlinx.coroutines.launch

class MainViewModel(initialState: MainState) : MavericksViewModel<MainState>(initialState) {

    private val repository = LocationRepository()

    init {
        fetchLocations()
    }

    private fun fetchLocations() = viewModelScope.launch {
        try {
            val response = repository.getLocations()
            setState { copy(locations = response.locations) }
        } catch (e: Exception) {
            setState { copy(errorMessage = e.message) }
        }
    }

    fun fetchRoutes(origin: String, destination: String) = viewModelScope.launch {
        try {
            val response = repository.getRoutes(origin, destination)
            when (response) {
                is RouteSuccessResponse -> {
                    val routeState: List<RouteState> = processRouteResponse(response.routes)
                    setState {
                        copy(
                            selectedOrigin = origin,
                            selectedDestination = destination,
                            routeStates = routeState,
                            isNavigateToMap = true
                        )
                    }
                }

                is ErrorResponse -> {
                    setState {
                        copy(
                            errorCode = response.code,
                            errorMessage = response.message,
                            selectedOrigin = origin,
                            selectedDestination = destination
                        )
                    }
                }
            }
        } catch (e: Exception) {
            setState {
                copy(
                    errorCode = 4041,
                    errorMessage = e.message ?: "unknown_error",
                    selectedOrigin = origin,
                    selectedDestination = destination
                )
            }
        }
    }

    fun clearError() {
        setState {
            copy(
                errorCode = null,
                errorMessage = null,
                selectedOrigin = null,
                selectedDestination = null,
                isNavigateToMap = false
            )
        }
    }

    fun fetchDistanceTime(origin: String, destination: String) = viewModelScope.launch {
        try {
            val response = repository.getDistanceTime(origin, destination)
            setState {
                copy(
                    distance = response.distance,
                    time = response.time
                )
            }
        } catch (e: Exception) {
            setState { copy(errorMessage = e.message) }
        }
    }
}