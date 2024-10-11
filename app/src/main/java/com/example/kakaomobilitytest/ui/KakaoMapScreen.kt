package com.example.kakaomobilitytest.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                                return 15 // 줌 레벨 설정
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
)  {
    // 1. RouteLineLayer 가져오기
    val routeLineManager = kakaoMap.routeLineManager
    val layer: RouteLineLayer? = routeLineManager?.layer

    val stylesSet: RouteLineStylesSet = RouteLineStylesSet.from(
        "JaonStyles",
        RouteLineStyles.from(RouteLineStyle.from(16f, RouteJam.toArgb()))
    )
    val segment = RouteLineSegment.from(
        Arrays.asList(
            LatLng.from(37.55595957732287, 126.97227318174524),
            LatLng.from(37.55584147066708, 126.97216162420102)
        )
    ).setStyles(stylesSet.getStyles(0))

    val options: RouteLineOptions = RouteLineOptions.from(segment).setStylesSet(stylesSet)
    layer?.addRouteLine(options)

//    val styleUnknown = RouteLineStyles.from(RouteLineStyle.from(16f, RouteUnknown.toArgb()))
//    val styleJam = RouteLineStyles.from(RouteLineStyle.from(16f, RouteJam.toArgb()))
//    val styleDelay = RouteLineStyles.from(RouteLineStyle.from(16f, RouteDelay.toArgb()))
//    val styleNormal = RouteLineStyles.from(RouteLineStyle.from(16f, RouteNormal.toArgb()))
//    val styleBlock = RouteLineStyles.from(RouteLineStyle.from(16f, RouteBlock.toArgb()))
//
//    val stylesSet: RouteLineStylesSet = RouteLineStylesSet.from(styleUnknown, styleJam, styleDelay, styleNormal, styleBlock)


///////////////////////////////////////////////
//    // 2. 각 구간에 대해 좌표 리스트 생성 및 경로 추가
//    val segments = mutableListOf<RouteLineSegment>()
//    for (i in startLngList.indices) {
//        val startLatLng = LatLng.from(startLngList[i], startLatList[i])
//        val endLatLng = LatLng.from(endLngList[i], endLatList[i])
//
//        // 교통 상태에 따른 색상 가져오기
//        val trafficColor = getTrafficColor(trafficStateList[i])
//
//        // RouteLineSegment 생성 및 스타일 설정
//        val routeLineStyles = RouteLineStyles.from(RouteLineStyle.from(16f, trafficColor))
//        val routeLineSegment =
//            RouteLineSegment.from(listOf(startLatLng, endLatLng)).setStyles(routeLineStyles)
//
//        segments.add(routeLineSegment)
//    }
//
//    // 3. 멀티스타일 RouteLineStylesSet 생성
//    val routeLineStylesSet = RouteLineStylesSet.from(
//        RouteLineStyles.from(RouteLineStyle.from(16f, RouteJam.toArgb())),
//        RouteLineStyles.from(
//            RouteLineStyle.from(
//                20f,
//                RouteDelay.toArgb(),
//                1f,
//                RouteNormal.toArgb()
//            )
//        )
//    )
//
//    val options = RouteLineOptions.from(segments).setStylesSet(routeLineStylesSet)
//    // 4. RouteLineOptions 생성 및 RouteLine 추가
//    val routeLineOptions = RouteLineOptions.from(segments).setStylesSet(routeLineStylesSet)
//    layer?.addRouteLine(routeLineOptions)
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
