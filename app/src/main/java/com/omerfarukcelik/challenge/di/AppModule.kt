package com.omerfarukcelik.challenge.di

import android.content.Context
import androidx.room.Room
import com.omerfarukcelik.challenge.data.local.dao.SatelliteDetailDao
import com.omerfarukcelik.challenge.data.local.database.SatelliteDatabase
import com.omerfarukcelik.challenge.data.repository.SatelliteRepositoryImpl
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindSatelliteRepository(
        satelliteRepositoryImpl: SatelliteRepositoryImpl
    ): ISatelliteRepository
    
    companion object {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context = context
        
        @Provides
        @Singleton
        fun provideSatelliteDatabase(@ApplicationContext context: Context): SatelliteDatabase {
            return Room.databaseBuilder(
                context,
                SatelliteDatabase::class.java,
                SatelliteDatabase.DATABASE_NAME
            ).build()
        }
        
        @Provides
        fun provideSatelliteDetailDao(database: SatelliteDatabase): SatelliteDetailDao {
            return database.satelliteDetailDao()
        }
    }
}
