package com.example.Vuelos.ui.screens.flight_screen

import com.example.Vuelos.model.Airport
import com.example.Vuelos.model.Favorite

data class FlightsUiState(
    val code: String = "",
    val favoriteList: List<Favorite> = emptyList(),
    val destinationList: List<Airport> = emptyList(),
    val departureAirport: Airport = Airport(),
)

