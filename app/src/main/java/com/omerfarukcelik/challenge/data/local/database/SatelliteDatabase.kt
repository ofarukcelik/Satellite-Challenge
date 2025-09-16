package com.omerfarukcelik.challenge.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omerfarukcelik.challenge.data.local.dao.SatelliteDetailDao
import com.omerfarukcelik.challenge.data.local.entity.SatelliteDetailEntity

@Database(
    entities = [SatelliteDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SatelliteDatabase : RoomDatabase() {

    abstract fun satelliteDetailDao(): SatelliteDetailDao

    companion object {
        const val DATABASE_NAME = "satellite_database"
    }
}
