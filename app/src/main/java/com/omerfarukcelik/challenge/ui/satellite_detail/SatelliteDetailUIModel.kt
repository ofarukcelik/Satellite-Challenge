package com.omerfarukcelik.challenge.ui.satellite_detail

import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel

data class SatelliteDetailUIModel(
    val id: Int,
    val name: String,
    val heightMass: String,
    val cost: String
)

fun SatelliteDetailDomainModel.toUIModel(): SatelliteDetailUIModel {
    return SatelliteDetailUIModel(
        id = this.id,
        name = this.name,
        heightMass = "${this.height}/${this.mass}",
        cost = this.costPerLaunch.toString()
    )
}
