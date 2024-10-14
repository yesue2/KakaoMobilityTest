package com.example.kakaomobilitytest.ui.maps

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
    val routeLineManager = kakaoMap.routeLineManager
    val layer: RouteLineLayer? = routeLineManager?.layer

    val stylesSet = createRouteLineStylesSet()
    val segments = mutableListOf<RouteLineSegment>()

    for (i in startLngList.indices) {
        val startLatLng = LatLng.from(startLatList[i], startLngList[i])
        val endLatLng = LatLng.from(endLatList[i], endLngList[i])
        val styleIndex = getStyleIndex(trafficStateList[i])

        val segment = RouteLineSegment.from(listOf(startLatLng, endLatLng))
            .setStyles(stylesSet.getStyles(styleIndex))

        segments.add(segment)
    }
    val options = RouteLineOptions.from(segments).setStylesSet(stylesSet)
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