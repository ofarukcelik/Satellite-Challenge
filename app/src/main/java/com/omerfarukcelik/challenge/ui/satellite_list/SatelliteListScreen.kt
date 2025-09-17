package com.omerfarukcelik.challenge.ui.satellite_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SatelliteListScreen(
    modifier: Modifier = Modifier,
    onSatelliteClick: (Int) -> Unit = {},
    viewModel: SatelliteViewModel = hiltViewModel()
) {
    val satelliteUIState by viewModel.satelliteUIState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSatellites()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.updateSearchQuery(query)
            },
            label = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = satelliteUIState) {
            is SatelliteUIState.Loading -> {
                LoadingState()
            }

            is SatelliteUIState.Error -> {
                ErrorState()
            }

            is SatelliteUIState.Empty -> {
                EmptyState(searchQuery = searchQuery)
            }

            is SatelliteUIState.LoadData -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(currentState.satellites) { satellite ->
                        Column {
                            SatelliteListItem(
                                satellite = satellite,
                                onClick = { onSatelliteClick(satellite.id) }
                            )

                            if (currentState.satellites.last() != satellite) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bir hata oluştu. Lütfen tekrar deneyin.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (searchQuery.isEmpty()) "No satellites found" else "No satellites match your search",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SatelliteListItem(
    satellite: SatelliteUIModel,
    onClick: () -> Unit
) {
    val alpha = if (satellite.active) 1f else 0.3f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color(satellite.statusColor))
        )

        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(
                text = satellite.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
            )
            Text(
                text = satellite.statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
            )
        }
    }
}
