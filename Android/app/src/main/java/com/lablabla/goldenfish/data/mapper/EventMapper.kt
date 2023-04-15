package com.lablabla.goldenfish.data.mapper

import com.lablabla.goldenfish.data.local.EventEntity
import com.lablabla.goldenfish.domain.model.Event

fun EventEntity.toEvent(): Event {
    return Event(
        id = id,
        name = name,
        cron = cron,
        duration = duration,
        zones = zones.map { it.toZone() }
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        cron = cron,
        duration = duration,
        zones = zones.map { it.toZoneEntity() }
    )
}
