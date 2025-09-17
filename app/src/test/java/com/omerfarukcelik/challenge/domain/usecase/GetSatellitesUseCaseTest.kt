package com.omerfarukcelik.challenge.domain.usecase

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSatellitesUseCaseTest {

    private lateinit var repository: ISatelliteRepository
    private lateinit var useCase: GetSatellitesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSatellitesUseCase(repository)
    }

    @Test
    fun `invoke should return satellites when repository returns data`() = runTest {
        // Given
        val expectedSatellites = listOf(
            SatelliteDomainModel(id = 1, active = true, name = "Satellite-1"),
            SatelliteDomainModel(id = 2, active = false, name = "Satellite-2")
        )
        coEvery { repository.getSatellites() } returns expectedSatellites

        // When
        val result = useCase()

        // Then
        assertEquals(expectedSatellites, result)
    }

    @Test
    fun `invoke should return empty list when repository returns empty list`() = runTest {
        // Given
        val expectedSatellites = emptyList<SatelliteDomainModel>()
        coEvery { repository.getSatellites() } returns expectedSatellites

        // When
        val result = useCase()

        // Then
        assertEquals(expectedSatellites, result)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        coEvery { repository.getSatellites() } throws expectedException

        // When & Then
        try {
            useCase()
            assert(false) { "Expected exception was not thrown" }
        } catch (e: RuntimeException) {
            assertEquals(expectedException.message, e.message)
        }
    }
}
