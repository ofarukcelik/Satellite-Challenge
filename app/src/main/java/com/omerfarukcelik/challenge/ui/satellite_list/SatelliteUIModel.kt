package com.omerfarukcelik.challenge.ui.satellite_list

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel

data class SatelliteUIModel(
    val id: Int,
    val active: Boolean,
    val name: String,
    val statusText: String,
    val statusColor: Long
)

fun SatelliteDomainModel.toUIModel(): SatelliteUIModel {
    return SatelliteUIModel(
        id = this.id,
        active = this.active,
        name = this.name,
        statusText = if (this.active) "Active" else "Passive",
        statusColor = if (this.active) 0xFF00FF00 else 0xFFFF0000
    )
}

fun List<SatelliteDomainModel>.toUIModel(): List<SatelliteUIModel> {
    return this.map { it.toUIModel() }
}
