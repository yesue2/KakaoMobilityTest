package com.example.kakaomobilitytest.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kakaomobilitytest.data.model.Location
import com.example.kakaomobilitytest.ui.theme.DarkColor
import com.example.kakaomobilitytest.ui.theme.PointColor


@Composable
fun List(
    locations: List<Location>,
    onLocationSelected: (origin: String, destination: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(locations) { location ->
            ListItem(
                origin = location.origin,
                destination = location.destination,
                onClick = { onLocationSelected(location.origin, location.destination) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListItem(origin: String, destination: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "출발지 : $origin",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DarkColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "📍 도착지 : $destination",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = PointColor,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
    }
}
