package com.example.kakaomobilitytest.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kakaomobilitytest.addLabelToMap
import com.example.kakaomobilitytest.addRouteToMap
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

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
                                    addLabelToMap(kakaoMap, startLngList[0], startLatList[0], endLngList[endLngList.size-1], endLatList[endLatList.size-1])
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