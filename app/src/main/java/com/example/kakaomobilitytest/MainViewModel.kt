package com.example.kakaomobilitytest

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.example.kakaomobilitytest.api.ApiClient
import com.example.kakaomobilitytest.api.LocationResponse
import com.example.kakaomobilitytest.api.RouteResponse
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
            val response: List<RouteResponse> = ApiClient.apiService.getRoutes(origin, destination)

            if (response.isNotEmpty()) {
                val points = response.joinToString(" ") { it.points } // 모든 경로의 points를 하나로 병합
                val trafficState = response.first().trafficState // 첫번째 경로의 교통 상태 사용

                // 경로가 있으면 상태 업데이트
                setState {
                    copy(
                        selectedOrigin = origin,
                        selectedDestination = destination,
                        points = points, // 경로의 points 저장
                        trafficState = trafficState, // 교통 상태 저장
                        shouldNavigateToMap = true // MapActivity로 이동할 준비 완료
                    )
                }
            } else {
                // 경로가 없으면 에러 상태 업데이트
                setState {
                    copy(
                        errorCode = 4041,
                        errorMessage = "not_found",
                        selectedOrigin = origin,
                        selectedDestination = destination
                    )
                }
            }
        } catch (e: Exception) {
            // API 호출 실패 시 에러 처리
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
        setState { copy(errorCode = null, errorMessage = null, selectedOrigin = null, selectedDestination = null, shouldNavigateToMap = false) }
    }


//    // 시간 및 거리 정보 가져오기
//    fun getDistanceTime(origin: String, destination: String) = viewModelScope.launch {
//        try {
//            val response = ApiClient.apiService.getDistanceTime(origin, destination)
//            setState { copy(distance = response.distance, time = response.time) } // 시간과 거리 상태 업데이트
//        } catch (e: Exception) {
//            setState { copy(error = e.message) } // 에러 처리
//        }
//    }
}