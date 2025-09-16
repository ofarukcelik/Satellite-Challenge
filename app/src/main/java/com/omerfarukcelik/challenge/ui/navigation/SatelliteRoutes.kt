package com.omerfarukcelik.challenge.ui.navigation

object SatelliteRoutes {
    const val SATELLITE_LIST = "satellite_list"
    const val SATELLITE_DETAIL = "satellite_detail/{satelliteId}"
    
    fun createSatelliteDetailRoute(satelliteId: Int): String {
        return "satellite_detail/$satelliteId"
    }
}
