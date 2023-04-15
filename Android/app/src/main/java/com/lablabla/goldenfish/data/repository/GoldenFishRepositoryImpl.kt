package com.lablabla.goldenfish.data.repository

import com.lablabla.goldenfish.data.local.GoldenFishDatabase
import com.lablabla.goldenfish.data.mapper.toEvent
import com.lablabla.goldenfish.data.mapper.toZone
import com.lablabla.goldenfish.domain.model.Event
import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.domain.repository.GoldenFishRepository
import com.lablabla.goldenfish.domain.repository.RemoteService
import com.lablabla.goldenfish.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoldenFishRepositoryImpl @Inject constructor(
    private val remote: RemoteService,
    db: GoldenFishDatabase
): GoldenFishRepository {

    private val zoneDao = db.zoneDao
    private val eventDao = db.eventDao

    init {
        remote.setCallback {
            // TODO: Add to local db
        }
    }

    override suspend fun sync() {
        remote.sync()
    }

    override suspend fun getZones(): Flow<Resource<List<Zone>>> {
        return flow {
            emit(Resource.Loading(true))
            val localZones = zoneDao.getZones()
            emit(Resource.Success(
                data = localZones.map { it.toZone() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun setZones(zones: List<Zone>) {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(): Flow<Resource<List<Event>>> {
        return flow {
            emit(Resource.Loading(true))
            val localEvents = eventDao.getEvents()
            emit(Resource.Success(
                data = localEvents.map { it.toEvent() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun removeEvent(event: Event) {
        TODO("Not yet implemented")
    }
}