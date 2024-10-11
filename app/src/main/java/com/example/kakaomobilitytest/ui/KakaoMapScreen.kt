package com.example.kakaomobilitytest.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
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
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import java.util.Arrays

@Composable
fun KakaoMapScreen(
    startLngList: List<Double>,
    startLatList: List<Double>,
    endLngList: List<Double>,
    endLatList: List<Double>,
    trafficStateList: List<String>,
    mapView: MapView
) {
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
                            override fun onMapDestroy() = Unit // 지도 API 가 정상적으로 종료될 때 호출
                            override fun onMapError(e: Exception?) {
                                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                                Log.d("KakaoMapScreen", "onMapError: ${e}")
                            }
//                            override fun onMapResumed() {
//                                // 지도 준비되면 경로 추가
//                            }
                        },
                        object : KakaoMapReadyCallback() {
                            // 인증 후 API 가 정상적으로 실행될 때 호출
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                Log.d("KakaoMapScreen", "onMapReady 실행")
                                // 지도 준비가 완료되면 경로 데이터를 추가하는 로직
                                if (startLngList.isNotEmpty() && trafficStateList.isNotEmpty()) {
                                    addRouteToMap(
                                        kakaoMap,
                                        startLngList,
                                        startLatList,
                                        endLngList,
                                        endLatList,
                                        trafficStateList
                                    )
                                }
                            }

                            override fun getPosition(): LatLng {
                                return LatLng.from(
                                    startLatList[0],
                                    startLngList[0]
                                ) // 시작점 좌표에서 지도 시작
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

fun addRouteToMap(
    kakaoMap: KakaoMap,
    startLngList: List<Double>,
    startLatList: List<Double>,
    endLngList: List<Double>,
    endLatList: List<Double>,
    trafficStateList: List<String>
) {
    // 1. RouteLineLayer 가져오기
    val routeLineManager = kakaoMap.routeLineManager
    val layer: RouteLineLayer? = routeLineManager?.layer

    // 2. RouteLineStylesSet 생성
    val stylesSet = createRouteLineStylesSet()

    // 3. 각 구간에 대해 RouteLineSegment 생성 및 경로 추가
    val segments = mutableListOf<RouteLineSegment>()

    for (i in startLngList.indices) {
        val startLatLng = LatLng.from(startLatList[i], startLngList[i])
        val endLatLng = LatLng.from(endLatList[i], endLngList[i])

        // 교통 상태에 따른 스타일 인덱스 가져오기
        val styleIndex = getStyleIndex(trafficStateList[i])

        // RouteLineSegment 생성 및 스타일 적용
        val segment = RouteLineSegment.from(listOf(startLatLng, endLatLng))
            .setStyles(stylesSet.getStyles(styleIndex))

        segments.add(segment)
    }

    // 4. RouteLineOptions 생성
    val options = RouteLineOptions.from(segments).setStylesSet(stylesSet)

    // 5. RouteLineLayer에 경로 추가
    layer?.addRouteLine(options)
}

fun createRouteLineStylesSet(): RouteLineStylesSet {
    val unknownStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteUnknown))
    val jamStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteJam))
    val delayStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteDelay))
    val slowStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteSlow))
    val normalStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteNormal))
    val blockStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteNormal))

    return RouteLineStylesSet.from(
        "TrafficStyles",
        unknownStyle, jamStyle, delayStyle, slowStyle, normalStyle, blockStyle
    )
}

fun getStyleIndex(trafficState: String): Int {
    return when (trafficState.trim().uppercase()) {
        "UNKNOWN" -> 0
        "JAM" -> 1
        "DELAY" -> 2
        "SLOW" -> 3
        "NORMAL" -> 4
        "BLOCK" -> 5
        else -> 0
    }.also { Log.d("TrafficStyle", "Traffic State: $trafficState, Style Index: $it") }
}