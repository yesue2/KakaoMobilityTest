package com.example.kakaomobilitytest

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.example.kakaomobilitytest.api.ApiClient
import com.example.kakaomobilitytest.api.LocationResponse
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
            setState { copy(error = e.message) } // 에러 상태 처리
        }
    }

//    // 경로 정보 가져오기
//    fun getRoutes(origin: String, destination: String) = viewModelScope.launch {
//        try {
//            val response = ApiClient.apiService.getRoutes(origin, destination)
//            setState { copy(selectedRoute = response.first()) } // 첫 번째 경로를 상태에 저장
//        } catch (e: Exception) {
//            setState { copy(error = e.message) } // 에러 처리
//        }
//    }
//
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