package com.example.kakaomobilitytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kakaomobilitytest.ui.components.AppBar
import com.example.kakaomobilitytest.ui.theme.KakaoMobilityTestTheme
import com.example.kakaomobilitytest.ui.screens.MainScreen
import com.example.kakaomobilitytest.ui.theme.DarkColor
import com.example.kakaomobilitytest.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                                MainScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}
