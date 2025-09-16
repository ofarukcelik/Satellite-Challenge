package com.omerfarukcelik.challenge.data.model

import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel

data class Satellite(
    val id: Int,
    val active: Boolean,
    val name: String
)

fun Satellite.toDomain(): SatelliteDomainModel {
    return SatelliteDomainModel(
        id = this.id,
        active = this.active,
        name = this.name
    )
}

fun List<Satellite>.toDomain(): List<SatelliteDomainModel> {
    return this.map { it.toDomain() }
}
