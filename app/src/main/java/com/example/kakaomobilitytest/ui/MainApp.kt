package com.example.kakaomobilitytest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.kakaomobilitytest.api.ApiClient
import com.example.kakaomobilitytest.api.RouteResponse
import com.example.kakaomobilitytest.api.processRouteResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MainApp() {
    // UI 구성
    Scaffold(
        topBar = {
            GlobalAppBar()
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                MainScreen()
            }
        }
    )
}

@Composable
fun GlobalAppBar() {
    AppBar(title = "카카오모빌리티 2차 과제 샘플") // 전역 AppBar 제목

}