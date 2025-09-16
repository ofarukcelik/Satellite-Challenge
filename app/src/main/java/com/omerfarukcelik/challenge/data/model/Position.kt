package com.omerfarukcelik.challenge.data.model

import com.google.gson.annotations.SerializedName
import com.omerfarukcelik.challenge.domain.model.PositionDomainModel

data class Position(
    @SerializedName("posX") val posX: Double,
    @SerializedName("posY") val posY: Double
)

data class SatellitePosition(
    val id: String,
    val positions: List<Position>
)

data class PositionResponse(
    val list: List<SatellitePosition>
)

fun Position.toDomain(): PositionDomainModel {
    return PositionDomainModel(
        posX = this.posX,
        posY = this.posY
    )
}
