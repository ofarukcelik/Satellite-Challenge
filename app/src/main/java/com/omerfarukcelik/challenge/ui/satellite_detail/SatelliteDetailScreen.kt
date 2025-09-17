package com.omerfarukcelik.challenge.ui.satellite_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SatelliteDetailScreen(
    satelliteId: Int,
    viewModel: SatelliteDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()

    LaunchedEffect(satelliteId) {
        viewModel.loadSatelliteDetail(satelliteId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val currentState = uiState) {
            is SatelliteDetailUIState.Loading -> {
                CircularProgressIndicator()
            }

            is SatelliteDetailUIState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentState.data.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Height/Mass:${currentState.data.heightMass}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Cost:${currentState.data.cost}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Last Position: ${currentPosition?.let { "(${it.posX},${it.posY})" } ?: "Loading..."}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is SatelliteDetailUIState.Error -> {
                Text(
                    text = "Error loading satellite",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
