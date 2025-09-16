package com.omerfarukcelik.challenge.di

import android.content.Context
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
    }
}
