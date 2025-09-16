package com.omerfarukcelik.challenge.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omerfarukcelik.challenge.data.model.Satellite
import com.omerfarukcelik.challenge.data.model.toDomain
import com.omerfarukcelik.challenge.domain.model.SatelliteDomainModel
import com.omerfarukcelik.challenge.domain.repository.ISatelliteRepository as InterfaceSatelliteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SatelliteRepositoryImpl @Inject constructor(
    private val context: Context
) : InterfaceSatelliteRepository {

    override suspend fun getSatellites(): List<SatelliteDomainModel> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("satellites.json")
                .bufferedReader()
                .use { it.readText() }

            val listType = object : TypeToken<List<Satellite>>() {}.type
            val satellites: List<Satellite> = Gson().fromJson(jsonString, listType)
            satellites.toDomain()
        } catch (e: IOException) {
            emptyList()
        }
    }
}
