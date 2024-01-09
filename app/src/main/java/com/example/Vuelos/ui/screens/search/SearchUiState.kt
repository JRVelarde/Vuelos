package com.example.Vuelos.ui.screens.search

import com.example.Vuelos.model.Airport
import com.example.Vuelos.model.Favorite

data class SearchUiState(
    val searchQuery: String = "",
    val selectedCode: String = "",
    val airportList: List<Airport> = emptyList(),
    val favoriteList: List<Favorite> = emptyList(),
)