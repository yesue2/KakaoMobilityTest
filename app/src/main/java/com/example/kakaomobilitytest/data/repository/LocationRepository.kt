package com.example.kakaomobilitytest.data.repository

import com.example.kakaomobilitytest.data.api.ApiClient
import com.example.kakaomobilitytest.data.model.ApiResponse
import com.example.kakaomobilitytest.data.model.DistanceTimeResponse
import com.example.kakaomobilitytest.data.model.ErrorResponse
import com.example.kakaomobilitytest.data.model.LocationResponse
import com.example.kakaomobilitytest.data.model.RouteResponse
import com.example.kakaomobilitytest.data.model.RouteSuccessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class LocationRepository {
    suspend fun getLocations(): LocationResponse {
        return withContext(Dispatchers.IO) {
            try {
                ApiClient.apiService.getLocations()
            } catch (e: IOException) {
                throw Exception("Location 정보를 받아오는 데 실패했습니다.")
            }
        }
    }

    suspend fun getRoutes(origin: String, destination: String): ApiResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getRoutes(origin, destination)
                if (response is List<*>) {
                    RouteSuccessResponse(response.filterIsInstance<RouteResponse>())
                } else {
                    val errorResponse = response as Map<String, Any>
                    ErrorResponse(errorResponse["code"] as? Int ?: 0, errorResponse["message"] as? String ?: "Unknown error")
                }
            } catch (e: IOException) {
                ErrorResponse(500, "경로 정보를 받아오는 데 실패했습니다.")
            }
        }
    }

    suspend fun getDistanceTime(origin: String, destination: String): DistanceTimeResponse {
        return withContext(Dispatchers.IO) {
            try {
                ApiClient.apiService.getDistanceTime(origin, destination)
            } catch (e: IOException) {
                throw Exception("거리와 시간 정보를 받아오는 데 실패했습니다.")
            }
        }
    }
}