package com.lablabla.goldenfish.data.local

import androidx.room.*

@Dao
interface EventDao {
    @Query("SELECT * FROM evententity")
    suspend fun getEvents(): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(zoneEntity: EventEntity)

    @Delete(entity = EventEntity::class)
    suspend fun removeEvent(zoneEntity: EventEntity)
}