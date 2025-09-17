package com.omerfarukcelik.challenge.ui.satellite_detail

import com.omerfarukcelik.challenge.domain.model.PositionDomainModel
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel
import com.omerfarukcelik.challenge.domain.usecase.GetSatelliteDetailUseCase
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitePositionsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SatelliteDetailViewModelTest {

    private lateinit var getSatelliteDetailUseCase: GetSatelliteDetailUseCase
    private lateinit var getSatellitePositionsUseCase: GetSatellitePositionsUseCase
    private lateinit var viewModel: SatelliteDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getSatelliteDetailUseCase = mockk()
        getSatellitePositionsUseCase = mockk()
        viewModel = SatelliteDetailViewModel(getSatelliteDetailUseCase, getSatellitePositionsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val initialState = viewModel.uiState.value
        assertTrue("Initial state should be Loading", initialState is SatelliteDetailUIState.Loading)
    }

    @Test
    fun `initial currentPosition should be null`() = runTest {
        val initialPosition = viewModel.currentPosition.value
        assertNull("Initial position should be null", initialPosition)
    }

    @Test
    fun `loadSatelliteDetail should emit Success when useCase returns data`() = runTest {
        val satelliteId = 1
        val satelliteDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns satelliteDetail
        coEvery { getSatellitePositionsUseCase(satelliteId) } returns flowOf()

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SatelliteDetailUIState.Success)
        val successState = state as SatelliteDetailUIState.Success
        assertEquals(satelliteId, successState.data.id)
        assertEquals("Test Satellite", successState.data.name)
        assertEquals("1000000", successState.data.cost)
        assertEquals("50/1000", successState.data.heightMass)
    }

    @Test
    fun `loadSatelliteDetail should emit Error when satellite not found`() = runTest {
        val satelliteId = 999
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns null

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is SatelliteDetailUIState.Error)
        val errorState = state as SatelliteDetailUIState.Error
        assertEquals("Satellite not found", errorState.message)
    }

    @Test
    fun `loadSatelliteDetail should emit Error when useCase throws exception`() = runTest {
        val satelliteId = 1
        val exception = RuntimeException("Network error")
        coEvery { getSatelliteDetailUseCase(satelliteId) } throws exception

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is SatelliteDetailUIState.Error)
        val errorState = state as SatelliteDetailUIState.Error
        assertEquals("Network error", errorState.message)
    }

    @Test
    fun `loadSatelliteDetail should emit Error when useCase throws exception with null message`() = runTest {
        val satelliteId = 1
        val exception = RuntimeException()
        coEvery { getSatelliteDetailUseCase(satelliteId) } throws exception

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is SatelliteDetailUIState.Error)
        val errorState = state as SatelliteDetailUIState.Error
        assertEquals("Unknown error", errorState.message)
    }

    @Test
    fun `loadSatelliteDetail should start position updates when satellite found`() = runTest {
        val satelliteId = 1
        val satelliteDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        val position = PositionDomainModel(posX = 10.0, posY = 20.0)
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns satelliteDetail
        coEvery { getSatellitePositionsUseCase(satelliteId) } returns flowOf(position)

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SatelliteDetailUIState.Success)
        
        val currentPosition = viewModel.currentPosition.value
        assertTrue("Current position should be updated", currentPosition != null)
        assertEquals(10.0, currentPosition!!.posX, 0.01)
        assertEquals(20.0, currentPosition!!.posY, 0.01)
    }

    @Test
    fun `position updates should emit multiple positions`() = runTest {
        val satelliteId = 1
        val satelliteDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        val positions = listOf(
            PositionDomainModel(posX = 10.0, posY = 20.0),
            PositionDomainModel(posX = 30.0, posY = 40.0),
            PositionDomainModel(posX = 50.0, posY = 60.0)
        )
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns satelliteDetail
        coEvery { getSatellitePositionsUseCase(satelliteId) } returns flowOf(*positions.toTypedArray())

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SatelliteDetailUIState.Success)
        
        val currentPosition = viewModel.currentPosition.value
        assertTrue("Current position should be updated", currentPosition != null)
        assertEquals(50.0, currentPosition!!.posX, 0.01)
        assertEquals(60.0, currentPosition!!.posY, 0.01)
    }

    @Test
    fun `position updates should handle empty flow`() = runTest {
        val satelliteId = 1
        val satelliteDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns satelliteDetail
        coEvery { getSatellitePositionsUseCase(satelliteId) } returns flowOf()

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SatelliteDetailUIState.Success)
        
        val currentPosition = viewModel.currentPosition.value
        assertNull("Current position should be null when no positions", currentPosition)
    }

    @Test
    fun `position updates should handle flow error`() = runTest {
        val satelliteId = 1
        val satelliteDetail = SatelliteDetailDomainModel(
            id = satelliteId,
            name = "Test Satellite",
            costPerLaunch = 1000000,
            height = 50,
            mass = 1000
        )
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns satelliteDetail
        coEvery { getSatellitePositionsUseCase(satelliteId) } returns flow {
            emit(PositionDomainModel(posX = 10.0, posY = 20.0))
            throw RuntimeException("Position error")
        }

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SatelliteDetailUIState.Success)
        
        val currentPosition = viewModel.currentPosition.value
        assertTrue("Current position should be updated", currentPosition != null)
        assertEquals(10.0, currentPosition!!.posX, 0.01)
        assertEquals(20.0, currentPosition!!.posY, 0.01)
    }

    @Test
    fun `loadSatelliteDetail should not start position updates when satellite not found`() = runTest {
        val satelliteId = 999
        coEvery { getSatelliteDetailUseCase(satelliteId) } returns null

        viewModel.loadSatelliteDetail(satelliteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is SatelliteDetailUIState.Error)
        
        val currentPosition = viewModel.currentPosition.value
        assertNull("Current position should be null when satellite not found", currentPosition)
    }
}
