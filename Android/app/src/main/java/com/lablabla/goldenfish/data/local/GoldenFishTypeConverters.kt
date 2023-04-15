package com.lablabla.goldenfish.data.local

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GoldenFishTypeConverters {
    private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
    private val membersType = Types.newParameterizedType(List::class.java, ZoneEntity::class.java)
    private val membersAdapter = moshi.adapter<List<ZoneEntity>>(membersType)

    @TypeConverter
    fun fromListToString(list: List<ZoneEntity>): String {
        return membersAdapter.toJson(list)
    }

    @TypeConverter
    fun toZoneEntity(dataString: String?): List<ZoneEntity> {
        if(dataString == null || dataString.isEmpty()) {
            return mutableListOf()
        }
        return membersAdapter.fromJson(dataString).orEmpty()
    }
}