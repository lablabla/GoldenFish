package com.lablabla.goldenfish.domain.model

data class DeviceStatus(
    val name: String,
    val isOnline: Boolean,
    val zones: List<Zone>? = null,
    val events: List<Event>? = null
)
