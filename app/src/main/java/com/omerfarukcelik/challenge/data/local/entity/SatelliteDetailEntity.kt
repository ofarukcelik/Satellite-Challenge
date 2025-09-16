package com.omerfarukcelik.challenge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellite_details")
data class SatelliteDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val costPerLaunch: Int,
    val height: Int,
    val mass: Int
)
