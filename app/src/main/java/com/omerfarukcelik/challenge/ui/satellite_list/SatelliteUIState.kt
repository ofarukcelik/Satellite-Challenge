package com.omerfarukcelik.challenge.ui.satellite_list

import com.omerfarukcelik.challenge.ui.satellite_list.SatelliteUIModel

sealed class SatelliteUIState {
    object Loading : SatelliteUIState()
    object Empty : SatelliteUIState()
    data class LoadData(val satellites: List<SatelliteUIModel>) : SatelliteUIState()
    data class Error(val exception: Throwable) : SatelliteUIState()
}