package com.omerfarukcelik.challenge.domain.repository

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel

interface ISatelliteRepository {
    suspend fun getSatellites(): List<SatelliteDomainModel>
    suspend fun getSatelliteDetail(satelliteId: Int): SatelliteDetailDomainModel?
}
