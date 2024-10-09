package com.example.kakaomobilitytest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kakaomobilitytest.AppBar

@Composable
fun MainApp() {
    Scaffold(
        topBar = {
            GlobalAppBar() // 전역적인 AppBar 설정
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
