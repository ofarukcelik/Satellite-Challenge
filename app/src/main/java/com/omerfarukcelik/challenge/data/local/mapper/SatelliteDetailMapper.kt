package com.omerfarukcelik.challenge.data.local.mapper

import com.omerfarukcelik.challenge.data.local.entity.SatelliteDetailEntity
import com.omerfarukcelik.challenge.domain.model.SatelliteDetailDomainModel

fun SatelliteDetailEntity.toDomain(): SatelliteDetailDomainModel {
    return SatelliteDetailDomainModel(
        id = this.id,
        name = this.name,
        costPerLaunch = this.costPerLaunch,
        height = this.height,
        mass = this.mass
    )
}

fun SatelliteDetailDomainModel.toEntity(): SatelliteDetailEntity {
    return SatelliteDetailEntity(
        id = this.id,
        name = this.name,
        costPerLaunch = this.costPerLaunch,
        height = this.height,
        mass = this.mass
    )
}
