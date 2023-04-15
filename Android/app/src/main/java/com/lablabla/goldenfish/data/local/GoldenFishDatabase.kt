package com.lablabla.goldenfish.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ZoneEntity::class, EventEntity::class],
    version = 1
)

@TypeConverters(GoldenFishTypeConverters::class)
abstract class GoldenFishDatabase: RoomDatabase() {
    abstract val zoneDao: ZoneDao
    abstract val eventDao: EventDao
}