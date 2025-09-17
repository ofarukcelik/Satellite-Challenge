package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.PositionDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSatellitePositionsUseCaseTest {

    private lateinit var repository: ISatelliteRepository
    private lateinit var useCase: GetSatellitePositionsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSatellitePositionsUseCase(repository)
    }

    @Test
    fun `invoke should emit positions every 3 seconds`() = runTest {
        // Given
        val satelliteId = 1
        val positions = listOf(
            PositionDomainModel(posX = 1.0, posY = 2.0),
            PositionDomainModel(posX = 3.0, posY = 4.0),
            PositionDomainModel(posX = 5.0, posY = 6.0)
        )
        coEvery { repository.getSatellitePositions(satelliteId) } returns positions

        // When & Then
        useCase(satelliteId).test {
            // First position should be emitted immediately
            assertEquals(positions[0], awaitItem())
            
            // Second position after 3 seconds
            assertEquals(positions[1], awaitItem())
            
            // Third position after another 3 seconds
            assertEquals(positions[2], awaitItem())
            
            // Flow should complete
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit empty list when repository returns empty list`() = runTest {
        // Given
        val satelliteId = 1
        val emptyPositions = emptyList<PositionDomainModel>()
        coEvery { repository.getSatellitePositions(satelliteId) } returns emptyPositions

        // When & Then
        useCase(satelliteId).test {
            // Should complete immediately with no emissions
            awaitComplete()
        }
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Given
        val satelliteId = 1
        val expectedException = RuntimeException("Network error")
        coEvery { repository.getSatellitePositions(satelliteId) } throws expectedException

        // When & Then
        useCase(satelliteId).test {
            // Should emit the exception
            assertEquals(expectedException, awaitError())
        }
    }
}
