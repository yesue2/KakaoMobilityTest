package com.example.kakaomobilitytest

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kakaomobilitytest.ui.AppBar
import com.example.kakaomobilitytest.ui.KakaoMapScreen
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.example.kakaomobilitytest.ui.theme.RouteBlock
import com.example.kakaomobilitytest.ui.theme.RouteDelay
import com.example.kakaomobilitytest.ui.theme.RouteJam
import com.example.kakaomobilitytest.ui.theme.RouteNormal
import com.example.kakaomobilitytest.ui.theme.RouteSlow
import com.example.kakaomobilitytest.ui.theme.RouteUnknown
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet

class MapActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // KakaoMap SDK 초기화
        KakaoMapSdk.init(this, BuildConfig.KAKAO_MAP_API_KEY)

        // Intent로 받은 값들을 각각 리스트로 변환
        val startLngList = intent.getDoubleArrayExtra("startLngList")?.toList() ?: emptyList()
        val startLatList = intent.getDoubleArrayExtra("startLatList")?.toList() ?: emptyList()
        val endLngList = intent.getDoubleArrayExtra("endLngList")?.toList() ?: emptyList()
        val endLatList = intent.getDoubleArrayExtra("endLatList")?.toList() ?: emptyList()
        val trafficStateList = intent.getStringArrayExtra("trafficStateList")?.toList() ?: emptyList()
        val distance = intent.getIntExtra("distance", 0)
        val time = intent.getIntExtra("time", 0)

        mapView = MapView(this) // MapView 인스턴스 생성

        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                    ) {
                    KakaoMapScreen(startLngList, startLatList, endLngList, endLatList, trafficStateList, distance, time, mapView)
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}
