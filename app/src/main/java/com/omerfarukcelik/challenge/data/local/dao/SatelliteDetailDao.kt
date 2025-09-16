package com.omerfarukcelik.challenge.data.local.dao

import androidx.room.*
import com.omerfarukcelik.challenge.data.local.entity.SatelliteDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SatelliteDetailDao {

    @Query("SELECT * FROM satellite_details WHERE id = :satelliteId")
    suspend fun getSatelliteDetail(satelliteId: Int): SatelliteDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSatelliteDetail(satelliteDetail: SatelliteDetailEntity)
}
