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

        val points = intent.getStringExtra("points") // 경로 좌표 정보
        val trafficState = intent.getStringExtra("trafficState") // 교통 상태 정보

        mapView = MapView(this) // MapView 인스턴스 생성

        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KakaoMapScreen(points, trafficState, mapView)
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


@Composable
fun KakaoMapScreen(points: String?, trafficState: String?, mapView: MapView) {
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
                                if (points != null && trafficState != null) {
                                    addRouteToMap(points, trafficState)
                                }
                            }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                // 지도 준비가 완료되면 경로 데이터를 추가하는 로직
                                if (points != null && trafficState != null) {
                                    addRouteToMap(kakaoMap, points, trafficState)
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

fun addRouteToMap(kakaoMap: KakaoMap, points: String, trafficState: String) {
    // 1. RouteLineLayer 가져오기
    val layer = kakaoMap.routeLineManager

    // 2. 좌표 리스트로 변환 (스페이스와 콤마를 기준으로 좌표 분리)
    val latLngList = points.split(" ").map {
        val (lng, lat) = it.split(",")
        LatLng.from(lng.toDouble(), lat.toDouble())
    }

    // 3. trafficState에 따른 색상 가져오기
    val trafficColor = getTrafficColor(trafficState)

    // 4. RouteLineStylesSet 생성
    val routeLineStyles = RouteLineStyles.from(RouteLineStyle.from(16f, trafficColor))
    val routeLineStylesSet = RouteLineStylesSet.from(routeLineStyles)

    // 5. RouteLineSegment 생성
    val routeLineSegment = RouteLineSegment.from(latLngList).setStyles(routeLineStyles)

    // 6. RouteLineOptions 생성
    val routeLineOptions = RouteLineOptions.from(routeLineSegment)
        .setStylesSet(routeLineStylesSet)

    // 7. RouteLineLayer에 경로 추가
    layer?.addRouteLine(routeLineOptions)
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
