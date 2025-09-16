package com.omerfarukcelik.challenge.ui.satellite_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omerfarukcelik.challenge.domain.model.PositionDomainModel
import com.omerfarukcelik.challenge.domain.usecase.GetSatelliteDetailUseCase
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitePositionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val getSatelliteDetailUseCase: GetSatelliteDetailUseCase,
    private val getSatellitePositionsUseCase: GetSatellitePositionsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<SatelliteDetailUIState>(SatelliteDetailUIState.Loading)
    val uiState: StateFlow<SatelliteDetailUIState> = _uiState.asStateFlow()
    
    private val _currentPosition = MutableStateFlow<PositionDomainModel?>(null)
    val currentPosition: StateFlow<PositionDomainModel?> = _currentPosition.asStateFlow()
    
    fun loadSatelliteDetail(satelliteId: Int) {
        viewModelScope.launch {
            _uiState.value = SatelliteDetailUIState.Loading
            try {
                val satelliteDetail = getSatelliteDetailUseCase(satelliteId)
                
                if (satelliteDetail != null) {
                    val uiModel = satelliteDetail.toUIModel()
                    _uiState.value = SatelliteDetailUIState.Success(uiModel)
                    
                    // Start position updates
                    startPositionUpdates(satelliteId)
                } else {
                    _uiState.value = SatelliteDetailUIState.Error("Satellite not found")
                }
            } catch (e: Exception) {
                _uiState.value = SatelliteDetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    private fun startPositionUpdates(satelliteId: Int) {
        viewModelScope.launch {
            getSatellitePositionsUseCase(satelliteId)
                .catch { e ->
                    // Handle error if needed
                }
                .collect { position ->
                    _currentPosition.value = position
                }
        }
    }
}

sealed class SatelliteDetailUIState {
    object Loading : SatelliteDetailUIState()
    data class Success(val data: SatelliteDetailUIModel) : SatelliteDetailUIState()
    data class Error(val message: String) : SatelliteDetailUIState()
}
