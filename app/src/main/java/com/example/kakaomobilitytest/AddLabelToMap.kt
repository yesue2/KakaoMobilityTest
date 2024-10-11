package com.example.kakaomobilitytest

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

fun addLabelToMap(
    kakaoMap: KakaoMap,
    startLng: Double,
    startLat: Double,
    endLng: Double,
    endLat: Double
) {
    val labelManager = kakaoMap.labelManager
    val layer: LabelLayer? = labelManager?.layer

    val startStyle: LabelStyles? = LabelStyles.from(LabelStyle.from(R.drawable.start))
    val endStyle: LabelStyles? = LabelStyles.from(LabelStyle.from(R.drawable.end))

    val options1 = LabelOptions.from(LatLng.from(startLat, startLng)).setStyles(startStyle)
    val options2 = LabelOptions.from(LatLng.from(endLat, endLng)).setStyles(endStyle)

    layer?.addLabel(options1)
    layer?.addLabel(options2)
}

