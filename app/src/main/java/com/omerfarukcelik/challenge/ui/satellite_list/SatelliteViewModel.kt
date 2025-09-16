package com.omerfarukcelik.challenge.ui.satellite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omerfarukcelik.challenge.domain.usecase.GetSatellitesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _allSatellites = MutableStateFlow<List<SatelliteUIModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _satellitesStatus = MutableStateFlow<StatusIO<List<SatelliteUIModel>>>(StatusIO.Loading)
    val satellitesStatus: StateFlow<StatusIO<List<SatelliteUIModel>>> = _satellitesStatus.asStateFlow()

    val filteredSatellites: StateFlow<List<SatelliteUIModel>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .map { query ->
            if (query.isEmpty()) {
                _allSatellites.value
            } else {
                _allSatellites.value.filter { satellite ->
                    satellite.name.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadSatellites()
    }

    private fun loadSatellites() {
        viewModelScope.launch {
            _satellitesStatus.value = StatusIO.Loading
            try {
                val satellitesList = getSatellitesUseCase()
                val uiModels = satellitesList.toUIModel()
                _allSatellites.value = uiModels
                _satellitesStatus.value = if (uiModels.isEmpty()) {
                    StatusIO.Empty
                } else {
                    StatusIO.Success(uiModels)
                }
            } catch (e: Exception) {
                _satellitesStatus.value = StatusIO.Error(e)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}