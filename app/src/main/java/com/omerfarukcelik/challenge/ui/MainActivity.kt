package com.omerfarukcelik.challenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omerfarukcelik.challenge.ui.navigation.SatelliteRoutes
import com.omerfarukcelik.challenge.ui.satellite_detail.SatelliteDetailScreen
import com.omerfarukcelik.challenge.ui.satellite_list.SatelliteListScreen
import com.omerfarukcelik.challenge.ui.theme.ChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = SatelliteRoutes.SATELLITE_LIST,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(SatelliteRoutes.SATELLITE_LIST) {
                                SatelliteListScreen(
                                    onSatelliteClick = { satelliteId ->
                                        navController.navigate(
                                            SatelliteRoutes.createSatelliteDetailRoute(satelliteId)
                                        )
                                    }
                                )
                            }

                            composable(SatelliteRoutes.SATELLITE_DETAIL) { backStackEntry ->
                                val satelliteId = backStackEntry.arguments?.getString("satelliteId")
                                    ?.toIntOrNull() ?: 0
                                SatelliteDetailScreen(satelliteId = satelliteId)
                            }
                        }
                    }
                }
            }
        }
    }
}