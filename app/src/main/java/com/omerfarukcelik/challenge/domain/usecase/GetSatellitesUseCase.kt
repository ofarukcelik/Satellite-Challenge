package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import javax.inject.Inject

class GetSatellitesUseCase @Inject constructor(
    private val repository: ISatelliteRepository
) {
    suspend operator fun invoke(): List<SatelliteDomainModel> {
        return repository.getSatellites()
    }
}
