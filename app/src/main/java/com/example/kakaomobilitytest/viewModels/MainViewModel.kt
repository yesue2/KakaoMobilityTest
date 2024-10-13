package com.example.kakaomobilitytest.viewModels

import com.airbnb.mvrx.MavericksViewModel
import com.example.kakaomobilitytest.data.model.*
import com.example.kakaomobilitytest.data.repository.LocationRepository
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
            val apiResponse = repository.getRoutes(origin, destination)

            when (apiResponse) {
                is RouteSuccessResponse -> {
                    val routeState: List<RouteState> = processRouteResponse(apiResponse.routes)
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
                            errorCode = apiResponse.code,
                            errorMessage = apiResponse.message,
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
            setState { copy(distance = response.distance, time = response.time) }
        } catch (e: Exception) {
            setState { copy(errorMessage = e.message) }
        }
    }
}
