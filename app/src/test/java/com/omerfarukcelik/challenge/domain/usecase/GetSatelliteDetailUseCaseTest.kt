package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetSatelliteDetailUseCaseTest {

    private lateinit var repository: ISatelliteRepository
    private lateinit var useCase: GetSatelliteDetailUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSatelliteDetailUseCase(repository)
    }

    @Test
    fun `invoke should return satellite detail when repository returns data`() = runTest {
        // Given
        val satelliteId = 1
        val expectedDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        coEvery { repository.getSatelliteDetail(satelliteId) } returns expectedDetail

        // When
        val result = useCase(satelliteId)

        // Then
        assertEquals(expectedDetail, result)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // Given
        val satelliteId = 999
        coEvery { repository.getSatelliteDetail(satelliteId) } returns null

        // When
        val result = useCase(satelliteId)

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Given
        val satelliteId = 1
        val expectedException = RuntimeException("Database error")
        coEvery { repository.getSatelliteDetail(satelliteId) } throws expectedException

        // When & Then
        try {
            useCase(satelliteId)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: RuntimeException) {
            assertEquals(expectedException.message, e.message)
        }
    }
}
