package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import javax.inject.Inject

class GetSatelliteDetailUseCase @Inject constructor(
    private val repository: ISatelliteRepository
) {
    suspend operator fun invoke(satelliteId: Int): SatelliteDetailDomainModel? {
        return repository.getSatelliteDetail(satelliteId)
    }
}
