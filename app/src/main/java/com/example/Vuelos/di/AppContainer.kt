package com.example.Vuelos.di

import android.content.Context
import com.example.Vuelos.data.FlightDatabase
import com.example.Vuelos.data.FlightRepository
import com.example.Vuelos.data.OfflineFlightRepository

interface AppContainer {
    val flightRepository: FlightRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineFlightRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [FlightRepository]
     */
    override val flightRepository: FlightRepository by lazy {
        OfflineFlightRepository(FlightDatabase.getDatabase(context).flightDao())
    }
}