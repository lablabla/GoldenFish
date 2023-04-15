package com.lablabla.goldenfish.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lablabla.goldenfish.domain.model.Zone

@Entity
data class EventEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val cron: String,
    val duration: Int,
    val zones: List<ZoneEntity>)
