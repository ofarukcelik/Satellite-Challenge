package com.omerfarukcelik.challenge.ui.satellite_list

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SatelliteViewModelTest {

    private lateinit var getSatellitesUseCase: GetSatellitesUseCase
    private lateinit var viewModel: SatelliteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getSatellitesUseCase = mockk()
        viewModel = SatelliteViewModel(getSatellitesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val initialState = viewModel.satelliteUIState.first { true }
        assertTrue("Initial state should be Loading", initialState is SatelliteUIState.Loading)
    }

    @Test
    fun `loadSatellites should emit LoadData when useCase returns data`() = runTest {
        val satellites = listOf(
            SatelliteDomainModel(id = 1, active = true, name = "Satellite-1"),
            SatelliteDomainModel(id = 2, active = false, name = "Satellite-2")
        )
        coEvery { getSatellitesUseCase() } returns satellites

        viewModel.loadSatellites()

        val loadDataState = viewModel.satelliteUIState.first { it is SatelliteUIState.LoadData } as SatelliteUIState.LoadData
        assertEquals(2, loadDataState.satellites.size)
        assertEquals("Satellite-1", loadDataState.satellites[0].name)
        assertEquals("Satellite-2", loadDataState.satellites[1].name)
        assertEquals(true, loadDataState.satellites[0].active)
        assertEquals(false, loadDataState.satellites[1].active)
    }

    @Test
    fun `loadSatellites should emit Empty when useCase returns empty list`() = runTest {
        coEvery { getSatellitesUseCase() } returns emptyList()

        viewModel.loadSatellites()

        val emptyState = viewModel.satelliteUIState.first { it is SatelliteUIState.Empty }
        assertTrue("State should be Empty", emptyState is SatelliteUIState.Empty)
    }

    @Test
    fun `loadSatellites should emit Error when useCase throws exception`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { getSatellitesUseCase() } throws exception

        viewModel.loadSatellites()

        val errorState = viewModel.satelliteUIState.first { it is SatelliteUIState.Error } as SatelliteUIState.Error
        assertEquals(exception, errorState.exception)
    }

    @Test
    fun `updateSearchQuery should update search query`() = runTest {
        val testQuery = "test search"

        viewModel.updateSearchQuery(testQuery)

        val searchQuery = viewModel.searchQuery.first { it == testQuery }
        assertEquals(testQuery, searchQuery)
    }

    @Test
    fun `loadSatellites should store satellites in allSatellites`() = runTest {
        val satellites = listOf(
            SatelliteDomainModel(id = 1, active = true, name = "Starship-1"),
            SatelliteDomainModel(id = 2, active = false, name = "Dragon-1")
        )
        coEvery { getSatellitesUseCase() } returns satellites

        viewModel.loadSatellites()
        val loadDataState = viewModel.satelliteUIState.first { it is SatelliteUIState.LoadData } as SatelliteUIState.LoadData

        assertEquals(2, loadDataState.satellites.size)
        assertEquals("Starship-1", loadDataState.satellites[0].name)
        assertEquals("Dragon-1", loadDataState.satellites[1].name)
    }

    @Test
    fun `searchQuery should be updated correctly`() = runTest {
        val testQuery = "test search"

        viewModel.updateSearchQuery(testQuery)

        val searchQuery = viewModel.searchQuery.first { it == testQuery }
        assertEquals(testQuery, searchQuery)
    }
}
