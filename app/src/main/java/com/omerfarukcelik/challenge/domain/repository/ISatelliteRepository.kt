package com.omerfarukcelik.challenge.domain.repository

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel

interface ISatelliteRepository {
    suspend fun getSatellites(): List<SatelliteDomainModel>
}
