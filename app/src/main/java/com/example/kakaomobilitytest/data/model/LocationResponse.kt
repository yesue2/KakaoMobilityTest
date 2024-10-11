package com.example.kakaomobilitytest.data.model

data class LocationResponse(
    val locations: List<Location>
)

data class Location(
    val origin: String,
    val destination: String
)
