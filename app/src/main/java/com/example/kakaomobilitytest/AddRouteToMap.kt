package com.example.kakaomobilitytest

import com.example.kakaomobilitytest.ui.theme.RouteBlock
import com.example.kakaomobilitytest.ui.theme.RouteDelay
import com.example.kakaomobilitytest.ui.theme.RouteJam
import com.example.kakaomobilitytest.ui.theme.RouteNormal
import com.example.kakaomobilitytest.ui.theme.RouteSlow
import com.example.kakaomobilitytest.ui.theme.RouteUnknown
import com.example.kakaomobilitytest.ui.theme.StrokeColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet


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
    val unknownStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteUnknown, 2f, StrokeColor))
    val jamStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteJam, 2f, StrokeColor))
    val delayStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteDelay, 2f, StrokeColor))
    val slowStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteSlow, 2f, StrokeColor))
    val normalStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteNormal, 2f, StrokeColor))
    val blockStyle = RouteLineStyles.from(RouteLineStyle.from(16f, RouteBlock, 2f, StrokeColor))

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
    }
}