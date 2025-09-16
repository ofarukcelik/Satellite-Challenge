package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.PositionDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetSatellitePositionsUseCase @Inject constructor(
    private val repository: ISatelliteRepository
) {
    operator fun invoke(satelliteId: Int): Flow<PositionDomainModel> = flow {
        // Get all positions for the satellite once
        val positions = repository.getSatellitePositions(satelliteId)
        
        positions.forEach { position ->
            emit(position)
            delay(3000) // 3 seconds delay
        }
    }
}
