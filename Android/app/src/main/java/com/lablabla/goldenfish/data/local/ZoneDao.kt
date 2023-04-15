package com.lablabla.goldenfish.data.local

import androidx.room.*

@Dao
interface ZoneDao {
    @Query("SELECT * FROM zoneentity")
    suspend fun getZones(): List<ZoneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addZone(zoneEntity: ZoneEntity)

    @Delete(entity = ZoneEntity::class)
    suspend fun removeZone(zoneEntity: ZoneEntity)
}