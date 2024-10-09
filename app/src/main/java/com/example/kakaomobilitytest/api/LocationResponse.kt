package com.example.kakaomobilitytest.api

data class LocationResponse(
    val locations: List<Location>
)

data class Location(
    val origin: String,
    val destination: String
)
