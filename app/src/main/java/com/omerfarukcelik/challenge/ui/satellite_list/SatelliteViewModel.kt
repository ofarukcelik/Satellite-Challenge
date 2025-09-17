package com.omerfarukcelik.challenge.ui.satellite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SatelliteViewModel @Inject constructor(
    private val getSatellitesUseCase: GetSatellitesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allSatellites = MutableStateFlow<List<SatelliteUIModel>>(emptyList())
    private val _satelliteUIState = MutableStateFlow<SatelliteUIState>(SatelliteUIState.Loading)

    val satelliteUIState: StateFlow<SatelliteUIState> = _satelliteUIState
        .combine(_searchQuery.debounce(300).distinctUntilChanged()) { state, query ->
            when (state) {
                is SatelliteUIState.LoadData -> {
                    val filteredSatellites = if (query.isEmpty()) {
                        _allSatellites.value
                    } else {
                        _allSatellites.value.filter { satellite ->
                            satellite.name.contains(query, ignoreCase = true)
                        }
                    }
                    SatelliteUIState.LoadData(filteredSatellites)
                }
                else -> state
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _satelliteUIState.value
        )


    fun loadSatellites() {
        viewModelScope.launch {
            _satelliteUIState.value = SatelliteUIState.Loading
            try {
                val satellitesList = getSatellitesUseCase()
                val uiModels = satellitesList.toUIModel()
                _allSatellites.value = uiModels
                _satelliteUIState.value = if (uiModels.isEmpty()) {
                    SatelliteUIState.Empty
                } else {
                    SatelliteUIState.LoadData(uiModels)
                }
            } catch (e: Exception) {
                _satelliteUIState.value = SatelliteUIState.Error(e)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}