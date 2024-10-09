package com.example.kakaomobilitytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.kakaomobilitytest.api.Location

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen() // MainScreen 함수 호출
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = mavericksViewModel()) {
    val state by viewModel.collectAsState()

    when {
        state.error != null -> {
            Text(text = "Error: ${state.error}") // 에러 메시지 표시
        }
        state.locations.isNotEmpty() -> {
            LocationListScreen(locations = state.locations) { origin, destination ->
                viewModel.getRoutes(origin, destination) // 경로 조회
                viewModel.getDistanceTime(origin, destination) // 시간 및 거리 정보 조회
            }
        }
        else -> {
            Text(text = "Loading...") // 로딩 상태 처리
        }
    }
}

@Composable
fun LocationListScreen(
    locations: List<Location>,
    onSelectLocation: (String, String) -> Unit
) {
    Column {
        locations.forEach { location ->
            Button(onClick = { onSelectLocation(location.origin, location.destination) }) {
                Text(text = "${location.origin} -> ${location.destination}")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    KakaoMobilityTestTheme {
        MainScreen()
    }
}