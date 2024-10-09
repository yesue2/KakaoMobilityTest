package com.example.kakaomobilitytest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

const val MainRoute = "Main"

@Composable
fun MainScreen(
    navigateToKakaoMap: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = navigateToKakaoMap
        ) {
            Text(text = "카카오맵 이동")
        }
    }
}

class LocationListActivity {

}