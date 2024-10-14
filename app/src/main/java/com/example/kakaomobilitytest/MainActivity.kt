package com.example.kakaomobilitytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.viewModel
import com.example.kakaomobilitytest.ui.components.AppBar
import com.example.kakaomobilitytest.ui.screens.MainScreen
import com.example.kakaomobilitytest.ui.theme.DarkColor
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.example.kakaomobilitytest.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModel()

        setContent {
            KakaoMobilityTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkColor,
                ) {
                    Scaffold(
                        topBar = {
                            AppBar()
                        },
                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues)) {
                                MainScreen(mainViewModel)
                            }
                        }
                    )
                }
            }
        }
    }
}
