package com.example.kakaomobilitytest.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.mavericksViewModel
import com.airbnb.mvrx.compose.collectAsState
import com.example.kakaomobilitytest.api.Location
import com.example.kakaomobilitytest.MainViewModel
import com.example.kakaomobilitytest.MapActivity

@Composable
fun MainScreen(viewModel: MainViewModel = mavericksViewModel()) {
    val state by viewModel.collectAsState()

    // 경로가 있는 경우 MapActivity로 이동
    if (state.shouldNavigateToMap) {
        val context = LocalContext.current
        val intent = Intent(context, MapActivity::class.java).apply {
            // routeStates에서 시작점, 끝점 및 교통 상태를 추출하여 MapActivity로 전달
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
        viewModel.clearError()
    }

    when {
        state.locations.isNotEmpty() -> {
            LocationListScreen(
                locations = state.locations,
                onLocationSelected = { origin, destination ->
                    viewModel.getRoutes(origin, destination) // 경로 조회
                    viewModel.getDistanceTime(origin, destination) // 시간 및 거리 정보 조회
                }
            )
        }

        else -> {
            Text(text = "Loading...") // 로딩 상태 처리
        }
    }

    // 에러 메시지 처리 (4041 코드 시 바텀시트 표시)
    if (state.errorMessage != null) {
        // 여기에 getErrorMessage 호출 추가
        val apiName = "경로 조회 API" // 호출된 API 이름
        val errorMessageToShow = getErrorMessage(apiName, state.errorCode, state.errorMessage)

        CustomBottomSheetScreen(
            errorCode = state.errorCode ?: 4041, // 상태에서 에러 코드 가져오기
            errorMessage = errorMessageToShow, // 생성된 에러 메시지 사용
            origin = state.selectedOrigin ?: "",
            destination = state.selectedDestination ?: ""
        ) {
            viewModel.clearError()
        }
    }
}

fun getErrorMessage(apiName: String, errorCode: Int?, errorMessage: String?): String {
    return when {
        errorMessage?.contains("Unable to resolve host") == true -> {
            "네트워크 연결을 확인해주세요." // 네트워크 문제에 대한 간략한 메시지
        }
        errorMessage.isNullOrEmpty() -> {
            when (errorCode) {
                null -> "$apiName API에서 에러가 발생했습니다." // 에러 코드와 메시지가 없는 경우 API 이름 표시
                else -> "$apiName API에서 에러가 발생했습니다. (코드: $errorCode)"
            }
        }
        else -> errorMessage // 에러 메시지가 있는 경우 그대로 출력
    }
}



@Composable
fun LocationListScreen(locations: List<Location>, onLocationSelected: (String, String) -> Unit) {
    LazyColumn {
        items(locations) { location ->
            LocationItem(
                origin = location.origin,
                destination = location.destination,
                onClick = { onLocationSelected(location.origin, location.destination) }
            )
        }
    }
}

@Composable
fun LocationItem(origin: String, destination: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = "출발지 : $origin", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "도착지 : $destination", color = MaterialTheme.colorScheme.primary)
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
    }
}
