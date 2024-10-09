package com.example.kakaomobilitytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.KakaoMapSdk
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // KakaoMap SDK 초기화
        KakaoMapSdk.init(this, "c98a502af0c6d74b1fccdd06bead5437")

        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KakaoMapScreen()
                }
            }
        }
    }
}

@Composable
fun KakaoMapScreen() {
    val context = LocalContext.current
    val mapView = rememberMapView(context = context)

    Scaffold(
        topBar = {
            AppBar(title = "Kakao Map")
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { mapView }, // MapView를 AndroidView로 감싸서 표시
            )
        }
    }
}

@Composable
fun rememberMapView(context: android.content.Context): MapView {
    val mapView = remember {
        MapView(context).apply {
            start(
                object : MapLifeCycleCallback() {
                    override fun onMapDestroy() = Unit
                    override fun onMapError(e: Exception?) = Unit
                    override fun onMapResumed() = Unit
                },
                object : KakaoMapReadyCallback() {
                    override fun onMapReady(map: KakaoMap) {
                        // 지도가 준비되면 실행할 작업
                    }
                }
            )
        }
    }
    return mapView
}
