package com.example.kakaomobilitytest.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.kakaomobilitytest.ui.components.BottomSheet
import com.example.kakaomobilitytest.ui.components.List
import com.example.kakaomobilitytest.viewModels.MainViewModel
import com.example.kakaomobilitytest.viewModels.getErrorMessage

@Composable
fun MainScreen(viewModel: MainViewModel = mavericksViewModel()) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isNavigateToMap) {
        if (state.isNavigateToMap) {
            viewModel.navigateToMap(context)
        }
    }

    when {
        state.locations.isNotEmpty() -> {
            List(
                locations = state.locations,
                onLocationSelected = { origin, destination ->
                    viewModel.fetchRoutes(origin, destination)
                    viewModel.fetchDistanceTime(origin, destination)
                }
            )
        }
        else -> {
            Text(text = "Loading...")
        }
    }

    if (state.errorMessage != null) {
        val apiName = "경로 조회 API"
        val errorMessageToShow = getErrorMessage(apiName, state.errorCode, state.errorMessage)

        BottomSheet(
            state.errorCode ?: 4041,
            errorMessageToShow,
            state.selectedOrigin ?: "",
            state.selectedDestination ?: ""
        ) {
            viewModel.clearError()
        }
    }
}