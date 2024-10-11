package com.example.kakaomobilitytest

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.example.kakaomobilitytest.api.*
import kotlinx.coroutines.launch

class MainViewModel(initialState: MainState) : MavericksViewModel<MainState>(initialState) {

    init {
        getLocations()
    }

    // 출발지/도착지 리스트 가져오기
    private fun getLocations() = viewModelScope.launch {
        try {
            Log.d("MainViewModel", "API 호출 시작") // API 호출 시작 로그

            val response: LocationResponse = ApiClient.apiService.getLocations() // API 호출
            Log.d("MainViewModel", "API 호출 성공: ${response.locations.size}개의 위치 수신") // 응답 성공 로그

            setState { copy(locations = response.locations) }  // 상태 업데이트
        } catch (e: Exception) {
            Log.e("MainViewModel", "API 호출 실패: ${e.message}") // 에러 로그 출력
            setState { copy(errorMessage = e.message) } // 에러 상태 처리
        }
    }

    // 경로 정보 가져오기
    fun getRoutes(origin: String, destination: String) = viewModelScope.launch {
        try {
            Log.d("MainViewModel", "경로 조회 시작: $origin -> $destination") // 경로 조회 시작 로그

            // API 응답 받기
            val apiResponse = getRoutesResponse(origin, destination)

            when (apiResponse) {
                is RouteSuccessResponse -> {
                    // 정상적인 경로 응답 처리
                    val routeState: List<RouteState> = processRouteResponse(apiResponse.routes)
                    Log.d("MainViewModel", "State 업데이트: 경로 조회 성공") // 상태 업데이트 로그
                    setState {
                        copy(
                            selectedOrigin = origin,
                            selectedDestination = destination,
                            routeStates = routeState, // 경로 리스트 저장
                            shouldNavigateToMap = true // MapActivity로 이동할 준비
                        )
                    }
                }

                is ErrorResponse -> {
                    // 에러 응답 처리 (예: code 4041)
                    Log.e("MainViewModel", "경로 조회 실패: ${apiResponse.message}")
                    setState {
                        Log.d("MainViewModel", "State 업데이트: 경로 조회 실패") // 상태 업데이트 로그
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
            // 예외 처리 시 에러 로그 출력
            Log.e("MainViewModel", "경로 조회 실패: ${e.message}")
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

    // 에러 상태 초기화
    fun clearError() {
        setState {
            Log.d("MainViewModel", "State 업데이트: 에러 초기화 -> $this") // 상태 업데이트 후 전체 state 로그
            copy(
                errorCode = null,
                errorMessage = null,
                selectedOrigin = null,
                selectedDestination = null,
                shouldNavigateToMap = false
            )
        }
    }

    fun getDistanceTime(origin: String, destination: String) = viewModelScope.launch {
        try {
            val response: DistanceTimeResponse =
                ApiClient.apiService.getDistanceTime(origin, destination)
            setState { copy(distance = response.distance, time = response.time) } // 시간과 거리 상태 업데이트
        } catch (e: Exception) {
            setState { copy(errorMessage = e.message) }
        }
    }
}
