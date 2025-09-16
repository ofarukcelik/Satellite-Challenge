package com.omerfarukcelik.challenge.ui.satellite_detail

sealed class SatelliteDetailUIState {
    object Loading : SatelliteDetailUIState()
    data class Success(val data: SatelliteDetailUIModel) : SatelliteDetailUIState()
    data class Error(val message: String) : SatelliteDetailUIState()
}