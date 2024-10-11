package com.example.kakaomobilitytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.kakaomobilitytest.ui.screens.KakaoMapScreen
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.kakao.vectormap.MapView
import com.kakao.vectormap.KakaoMapSdk

class MapActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoMapSdk.init(this, BuildConfig.KAKAO_MAP_API_KEY)

        val startLngList = intent.getDoubleArrayExtra("startLngList")?.toList() ?: emptyList()
        val startLatList = intent.getDoubleArrayExtra("startLatList")?.toList() ?: emptyList()
        val endLngList = intent.getDoubleArrayExtra("endLngList")?.toList() ?: emptyList()
        val endLatList = intent.getDoubleArrayExtra("endLatList")?.toList() ?: emptyList()
        val trafficStateList = intent.getStringArrayExtra("trafficStateList")?.toList() ?: emptyList()
        val distance = intent.getIntExtra("distance", 0)
        val time = intent.getIntExtra("time", 0)

        mapView = MapView(this)

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
