package com.example.kakaomobilitytest.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kakaomobilitytest.ui.maps.addLabelToMap
import com.example.kakaomobilitytest.ui.maps.addRouteToMap
import com.example.kakaomobilitytest.ui.components.AppBar
import com.example.kakaomobilitytest.ui.theme.DarkColor
import com.example.kakaomobilitytest.ui.theme.PointColor
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

@Composable
fun MapScreen(
    startLngList: List<Double>,
    startLatList: List<Double>,
    endLngList: List<Double>,
    endLatList: List<Double>,
    trafficStateList: List<String>,
    distance: Int,
    time: Int,
    mapView: MapView
) {
    Scaffold(
        topBar = {
            AppBar()
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
                factory = { mapView },
                update = { mapView ->
                    mapView.start(
                        object : MapLifeCycleCallback() {
                            override fun onMapDestroy() = Unit
                            override fun onMapError(e: Exception?) {
                                Log.d("KakaoMapScreen", "onMapError: ${e}")
                            }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                kakaoMap.setGestureEnable(GestureType.Zoom, true)
                                if (startLngList.isNotEmpty() && trafficStateList.isNotEmpty()) {
                                    addRouteToMap(
                                        kakaoMap,
                                        startLngList,
                                        startLatList,
                                        endLngList,
                                        endLatList,
                                        trafficStateList
                                    )
                                    addLabelToMap(
                                        kakaoMap,
                                        startLngList[0],
                                        startLatList[0],
                                        endLngList[endLngList.size - 1],
                                        endLatList[endLatList.size - 1]
                                    )
                                }
                            }

                            override fun getPosition(): LatLng {
                                return LatLng.from(
                                    startLatList[startLatList.size/2],
                                    startLngList[startLngList.size/2]
                                )
                            }

                            override fun getZoomLevel(): Int {
                                return 11
                            }
                        }
                    )

                }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(DarkColor)
                    .padding(20.dp)
            ) {
                Column {
                    val timeText = formatTime(time)
                    val distanceText = formatDistance(distance)

                    Text(
                        text = "시간:  $timeText",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PointColor
                    )
                    Text(
                        text = "거리:  $distanceText",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PointColor
                    )
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainSeconds = seconds % 60
    return "$minutes 분 $remainSeconds 초"
}

fun formatDistance(distance: Int): String {
    return if (distance >= 1000) {
        val kilometers = distance / 1000
        val meters = distance % 1000
        if (meters == 0) "$kilometers km"
        else "$kilometers.$meters km"
    } else {
        "$distance m"
    }
}