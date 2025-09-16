package com.omerfarukcelik.challenge.ui.satellite_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omerfarukcelik.challenge.domain.usecase.GetSatelliteDetailUseCase
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val getSatelliteDetailUseCase: GetSatelliteDetailUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<SatelliteDetailUIState>(SatelliteDetailUIState.Loading)
    val uiState: StateFlow<SatelliteDetailUIState> = _uiState.asStateFlow()
    
    fun loadSatelliteDetail(satelliteId: Int) {
        viewModelScope.launch {
            _uiState.value = SatelliteDetailUIState.Loading
            try {
                val satelliteDetail = getSatelliteDetailUseCase(satelliteId)
                
                if (satelliteDetail != null) {
                    val uiModel = satelliteDetail.toUIModel()
                    _uiState.value = SatelliteDetailUIState.Success(uiModel)
                } else {
                    _uiState.value = SatelliteDetailUIState.Error("Satellite not found")
                }
            } catch (e: Exception) {
                _uiState.value = SatelliteDetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SatelliteDetailUIState {
    object Loading : SatelliteDetailUIState()
    data class Success(val data: SatelliteDetailUIModel) : SatelliteDetailUIState()
    data class Error(val message: String) : SatelliteDetailUIState()
}
