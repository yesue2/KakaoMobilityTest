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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kakaomobilitytest.ui.AppBar
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.KakaoMapSdk
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.example.kakaomobilitytest.ui.theme.RouteBlock
import com.example.kakaomobilitytest.ui.theme.RouteDelay
import com.example.kakaomobilitytest.ui.theme.RouteJam
import com.example.kakaomobilitytest.ui.theme.RouteNormal
import com.example.kakaomobilitytest.ui.theme.RouteSlow
import com.example.kakaomobilitytest.ui.theme.RouteUnknown
import com.kakao.vectormap.LatLng
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

        Log.d("MapActivity", "startLngList: $startLngList")
        Log.d("MapActivity", "startLatList: $startLatList")
        Log.d("MapActivity", "endLngList: $endLngList")
        Log.d("MapActivity", "endLatList: $endLatList")
        Log.d("MapActivity", "trafficStateList: $trafficStateList")


        mapView = MapView(this) // MapView 인스턴스 생성

        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KakaoMapScreen(startLngList, startLatList, endLngList, endLatList, trafficStateList, mapView)
                }
            }
        }
    }

    // Activity의 라이프사이클에 맞춰 지도 라이프사이클 호출
    override fun onStart() {
        super.onStart()
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() = Unit
                override fun onMapError(e: Exception?) = Unit
                override fun onMapResumed() = Unit
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    // 지도가 준비되면 경로 추가 등의 작업을 여기서 실행합니다.
                }

                override fun getPosition(): LatLng {
                    return LatLng.from(37.5665, 126.9780) // 서울 시청 좌표
                }

                override fun getZoomLevel(): Int {
                    return 10 // 줌 레벨 설정
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroy() {
        mapView.finish()
        super.onDestroy()
    }
}

fun addRouteToMap(kakaoMap: KakaoMap, startLngList: List<Double>, startLatList: List<Double>, endLngList: List<Double>, endLatList: List<Double>, trafficStateList: List<String>) {
    // 1. RouteLineLayer 가져오기
    val layer: RouteLineLayer = kakaoMap.routeLineManager!!.layer

    // 2. 각 구간에 대해 좌표 리스트 생성 및 경로 추가
    for (i in startLngList.indices) {
        val startLatLng = LatLng.from(startLngList[i], startLatList[i])
        val endLatLng = LatLng.from(endLngList[i], endLatList[i])

        // 교통 상태에 따른 색상 가져오기
        val trafficColor = getTrafficColor(trafficStateList[i])

        // RouteLineStylesSet 생성
        val routeLineStyles = RouteLineStyles.from(RouteLineStyle.from(16f, trafficColor))
        val routeLineStylesSet = RouteLineStylesSet.from(routeLineStyles)

        // RouteLineSegment 생성
        val routeLineSegment = RouteLineSegment.from(listOf(startLatLng, endLatLng)).setStyles(routeLineStyles)

        // RouteLineOptions 생성
        val routeLineOptions = RouteLineOptions.from(routeLineSegment)
            .setStylesSet(routeLineStylesSet)

        // RouteLineLayer에 경로 추가
        layer?.addRouteLine(routeLineOptions)
    }
}

@Composable
fun KakaoMapScreen(startLngList: List<Double>, startLatList: List<Double>, endLngList: List<Double>, endLatList: List<Double>, trafficStateList: List<String>, mapView: MapView) {
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
                update = { mapView ->
                    mapView.start(
                        object : MapLifeCycleCallback() {
                            override fun onMapDestroy() = Unit
                            override fun onMapError(e: Exception?) = Unit
                            override fun onMapResumed() {
                                // 지도 준비되면 경로 추가
                            }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                // 지도 준비가 완료되면 경로 데이터를 추가하는 로직
                                if (startLngList.isNotEmpty() && trafficStateList.isNotEmpty()) {
                                    addRouteToMap(kakaoMap, startLngList, startLatList, endLngList, endLatList, trafficStateList)
                                }
                            }

                            override fun getPosition(): LatLng {
                                return LatLng.from(37.5665, 126.9780) // 서울 시청 좌표
                            }

                            override fun getZoomLevel(): Int {
                                return 10 // 줌 레벨 설정
                            }
                        }
                    )
                }
            )
        }
    }
}

fun getTrafficColor(trafficState: String?): Int {
    return when (trafficState) {
        "UNKNOWN" -> RouteUnknown.toArgb()
        "JAM" -> RouteJam.toArgb()
        "DELAY" -> RouteDelay.toArgb()
        "SLOW" -> RouteSlow.toArgb()
        "NORMAL" -> RouteNormal.toArgb()
        "BLOCK" -> RouteBlock.toArgb()
        else -> RouteUnknown.toArgb()
    }
}
