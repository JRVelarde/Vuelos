package com.example.Vuelos.ui.screens.flight_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.Vuelos.FlightApplication
import com.example.Vuelos.data.FlightRepository
import com.example.Vuelos.model.Favorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightViewModel(
    savedStateHandle: SavedStateHandle,
    val flightRepository: FlightRepository
): ViewModel()  {
    private val _uiState = MutableStateFlow(FlightsUiState())
    val uiState: StateFlow<FlightsUiState> = _uiState

    private val airportCode: String = savedStateHandle[FlightScreenDestination.codeArg] ?: ""

    var flightAdded: Boolean by mutableStateOf(false)

//    private var getFlightsJob: Job? = null

    init {
        viewModelScope.launch {
            processFlightList(airportCode)
        }
    }

    private fun processFlightList(airportCode: String) {

        viewModelScope.launch {
            val ff = flightRepository.getAllFavoritesFlights().toMutableStateList()
            val aa = flightRepository.getAllAirports().toMutableStateList()
            val departureAirport = aa.first { it.code == airportCode }
            _uiState.update {
                uiState.value.copy(
                    code = airportCode,
                    favoriteList = ff,
                    destinationList = aa,
                    departureAirport = departureAirport,
                )
            }
        }
    }

    fun addFavoriteFlight(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val favorite: Favorite = flightRepository.getSingleFavorite(departureCode, destinationCode)

            if (favorite == null) {
                val tmp = Favorite(
                    departureCode = departureCode,
                    destinationCode = destinationCode,
                )
                flightAdded = true
                flightRepository.insertFavoriteFlight(tmp)
            } else {
                flightAdded = false
                flightRepository.deleteFavoriteFlight(favorite)
            }

            // Cheating, I am forcing a Recomposition
            // I should be using Flow but am not sure how to atm
            val play = flightRepository.getAllFavoritesFlights()
            _uiState.update {
                uiState.value.copy(
                    favoriteList = play,
                )
            }
        }
    }


    // Notes: Question: At moment this is chuck of code is repeated in two files
    //  in QueryViewModel and in DetailsViewModel.
    //  what can I do/ place it so as not to have repeat code? I tried but I got a bunch of errors
    /**
     * Factory for BookshelfViewModel] that takes BookshelfRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication)
                val flightRepository = application.container.flightRepository
                FlightViewModel(
                    this.createSavedStateHandle(),
                    flightRepository = flightRepository
                )
            }
        }
    }
}