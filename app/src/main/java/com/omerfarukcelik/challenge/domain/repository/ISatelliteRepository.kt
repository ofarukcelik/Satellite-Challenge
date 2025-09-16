package com.omerfarukcelik.challenge.domain.repository

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel
import com.omerfarukcelik.challenge.domain.model.PositionDomainModel

interface ISatelliteRepository {
    suspend fun getSatellites(): List<SatelliteDomainModel>
    suspend fun getSatelliteDetail(satelliteId: Int): SatelliteDetailDomainModel?
    suspend fun getSatellitePositions(satelliteId: Int): List<PositionDomainModel>
}
