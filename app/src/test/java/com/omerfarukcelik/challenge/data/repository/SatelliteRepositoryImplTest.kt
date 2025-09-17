package com.omerfarukcelik.challenge.data.repository

import android.content.Context
import com.omerfarukcelik.challenge.data.local.dao.SatelliteDetailDao
import com.omerfarukcelik.challenge.data.model.Satellite
import com.omerfarukcelik.challenge.data.model.SatelliteDetail
import com.omerfarukcelik.challenge.data.model.Position
import com.omerfarukcelik.challenge.data.model.PositionResponse
import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel
import com.omerfarukcelik.challenge.domain.model.PositionDomainModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

class SatelliteRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var satelliteDetailDao: SatelliteDetailDao
    private lateinit var repository: SatelliteRepositoryImpl

    @Before
    fun setup() {
        context = mockk()
        satelliteDetailDao = mockk()
        repository = SatelliteRepositoryImpl(context, satelliteDetailDao, Dispatchers.IO)
    }

    @Test
    fun `getSatellites should return satellites when JSON is valid`() = runTest {
        // Given
        val jsonString = """
            [
                {"id": 1, "active": true, "name": "Satellite-1"},
                {"id": 2, "active": false, "name": "Satellite-2"}
            ]
        """.trimIndent()
        
        every { context.assets.open("satellites.json") } returns ByteArrayInputStream(jsonString.toByteArray())

        // When
        val result = repository.getSatellites()

        // Then
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(true, result[0].active)
        assertEquals("Satellite-1", result[0].name)
        assertEquals(2, result[1].id)
        assertEquals(false, result[1].active)
        assertEquals("Satellite-2", result[1].name)
    }

    @Test
    fun `getSatellites should return empty list when JSON is invalid`() = runTest {
        // Given
        every { context.assets.open("satellites.json") } throws IOException("File not found")

        // When
        val result = repository.getSatellites()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getSatelliteDetail should return cached data when available`() = runTest {
        // Given
        val satelliteId = 1
        val cachedDetail = com.omerfarukcelik.challenge.data.local.entity.SatelliteDetailEntity(
            id = satelliteId,
            name = "Cached Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        coEvery { satelliteDetailDao.getSatelliteDetail(satelliteId) } returns cachedDetail

        // When
        val result = repository.getSatelliteDetail(satelliteId)

        // Then
        assertEquals(satelliteId, result?.id)
        assertEquals("Cached Satellite", result?.name)
        assertEquals(1000000, result?.costPerLaunch)
        assertEquals(50, result?.height)
        assertEquals(1000, result?.mass)
    }

    @Test
    fun `getSatelliteDetail should return null when not found`() = runTest {
        // Given
        val satelliteId = 999
        coEvery { satelliteDetailDao.getSatelliteDetail(satelliteId) } returns null
        
        val jsonString = """
            [
                {"id": 1, "name": "Satellite-1", "cost_per_launch": 1000000, "height": 50, "mass": 1000}
            ]
        """.trimIndent()
        every { context.assets.open("satellites-detail.json") } returns ByteArrayInputStream(jsonString.toByteArray())

        // When
        val result = repository.getSatelliteDetail(satelliteId)

        // Then
        assertNull(result)
    }

    @Test
    fun `getSatellitePositions should return positions when JSON is valid`() = runTest {
        // Given
        val satelliteId = 1
        val jsonString = """
            {
                "list": [
                    {
                        "id": "1",
                        "positions": [
                            {"posX": 1.0, "posY": 2.0},
                            {"posX": 3.0, "posY": 4.0}
                        ]
                    }
                ]
            }
        """.trimIndent()
        
        every { context.assets.open("positions.json") } returns ByteArrayInputStream(jsonString.toByteArray())

        // When
        val result = repository.getSatellitePositions(satelliteId)

        // Then
        assertEquals(2, result.size)
        assertEquals(1.0, result[0].posX, 0.01)
        assertEquals(2.0, result[0].posY, 0.01)
        assertEquals(3.0, result[1].posX, 0.01)
        assertEquals(4.0, result[1].posY, 0.01)
    }

    @Test
    fun `getSatellitePositions should return empty list when satellite not found`() = runTest {
        // Given
        val satelliteId = 999
        val jsonString = """
            {
                "list": [
                    {
                        "id": "1",
                        "positions": [
                            {"posX": 1.0, "posY": 2.0}
                        ]
                    }
                ]
            }
        """.trimIndent()
        
        every { context.assets.open("positions.json") } returns ByteArrayInputStream(jsonString.toByteArray())

        // When
        val result = repository.getSatellitePositions(satelliteId)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getSatellitePositions should return empty list when JSON is invalid`() = runTest {
        // Given
        val satelliteId = 1
        every { context.assets.open("positions.json") } throws IOException("File not found")

        // When
        val result = repository.getSatellitePositions(satelliteId)

        // Then
        assertEquals(0, result.size)
    }
}
