package com.lablabla.goldenfish.data.mapper

import com.lablabla.goldenfish.data.local.ZoneEntity
import com.lablabla.goldenfish.domain.model.Zone

fun ZoneEntity.toZone(): Zone {
    return Zone(
        id = id,
        name = name,
        state = state
    )
}

fun Zone.toZoneEntity(): ZoneEntity {
    return ZoneEntity(
        id = id,
        name = name,
        state = state
    )
}