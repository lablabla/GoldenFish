package com.lablabla.goldenfish.domain.repository

import com.lablabla.goldenfish.domain.model.Event
import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.util.Resource
import kotlinx.coroutines.flow.Flow

interface GoldenFishRepository {

    suspend fun sync()

    suspend fun getZones(): Flow<Resource<List<Zone>>>

    suspend fun setZones(zones: List<Zone>)

    suspend fun getEvents(): Flow<Resource<List<Event>>>

    suspend fun addEvent(event: Event)

    suspend fun removeEvent(event: Event)
}