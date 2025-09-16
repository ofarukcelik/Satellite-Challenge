package com.omerfarukcelik.challenge.domain.model

data class SatelliteDetailDomainModel(
    val id: Int,
    val name: String,
    val costPerLaunch: Int,
    val height: Int,
    val mass: Int
)