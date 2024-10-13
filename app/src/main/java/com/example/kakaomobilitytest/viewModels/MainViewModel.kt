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

    fun navigateToMap(context: Context) {
        withState { state ->
            val intent = Intent(context, MapActivity::class.java).apply {
                val startLngList = state.routeStates.map { it.startLng }
                val startLatList = state.routeStates.map { it.startLat }
                val endLngList = state.routeStates.map { it.endLng }
                val endLatList = state.routeStates.map { it.endLat }
                val trafficStateList = state.routeStates.map { it.state }
                val distance = state.distance ?: 0
                val time = state.time ?: 0

                putExtra("startLngList", startLngList.toDoubleArray())
                putExtra("startLatList", startLatList.toDoubleArray())
                putExtra("endLngList", endLngList.toDoubleArray())
                putExtra("endLatList", endLatList.toDoubleArray())
                putExtra("trafficStateList", trafficStateList.toTypedArray())
                putExtra("distance", distance)
                putExtra("time", time)
            }

            context.startActivity(intent)
            clearError()
        }
    }
}

fun getErrorMessage(apiName: String, errorCode: Int?, errorMessage: String?): String {
    return when {
        errorMessage?.contains("Unable to resolve host") == true -> {
            "경로 설정 API 에러: 네트워크 연결을 확인해주세요."
        }
        errorMessage.isNullOrEmpty() -> {
            when (errorCode) {
                null -> "$apiName API에서 에러가 발생했습니다."
                else -> "$apiName API 에러 코드: $errorCode)"
            }
        }
        else -> "not_found"
    }
}
