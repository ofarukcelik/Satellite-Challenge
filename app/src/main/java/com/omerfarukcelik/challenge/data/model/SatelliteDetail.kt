package com.omerfarukcelik.challenge.data.model

import com.google.gson.annotations.SerializedName
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel

data class SatelliteDetail(
    val id: Int,
    @SerializedName("cost_per_launch") val costPerLaunch: Int,
    val height: Int,
    val mass: Int,
    val name: String
)

fun SatelliteDetail.toDomain(): SatelliteDetailDomainModel {
    return SatelliteDetailDomainModel(
        id = this.id,
        costPerLaunch = this.costPerLaunch,
        height = this.height,
        mass = this.mass,
        name = this.name
    )
}
