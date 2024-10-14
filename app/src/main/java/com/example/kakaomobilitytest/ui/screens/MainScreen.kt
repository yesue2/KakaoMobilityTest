package com.example.kakaomobilitytest.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.kakaomobilitytest.MapActivity
import com.example.kakaomobilitytest.Utils.getErrorMessage
import com.example.kakaomobilitytest.ui.components.AppBar
import com.example.kakaomobilitytest.ui.components.BottomSheet
import com.example.kakaomobilitytest.ui.components.List
import com.example.kakaomobilitytest.viewModels.MainViewModel
import java.util.logging.ErrorManager

@Composable
fun MainScreen(viewModel: MainViewModel = mavericksViewModel()) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isNavigateToMap) {
        if (state.isNavigateToMap) {
            val intent = Intent(context, MapActivity::class.java).apply {
                val startLngList = state.routeStates.map { it.startLng }
                val startLatList = state.routeStates.map { it.startLat }
                val endLngList = state.routeStates.map { it.endLng }
                val endLatList = state.routeStates.map { it.endLat }
                val trafficStateList = state.routeStates.map { it.state }
                val distance = state.distance ?: 0
                val time = state.time ?: 0

                putExtra("startLngList", startLngList.toDoubleArray())
                putExtra("startLatList", startLatList.toDoubleArray())
                putExtra("endLngList", endLngList.toDoubleArray())
                putExtra("endLatList", endLatList.toDoubleArray())
                putExtra("trafficStateList", trafficStateList.toTypedArray())
                putExtra("distance", distance)
                putExtra("time", time)
            }

            context.startActivity(intent)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            AppBar()
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
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
        }
    )
}
