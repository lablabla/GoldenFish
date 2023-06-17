package com.lablabla.goldenfish.domain.model

import java.util.Date
import kotlin.time.Duration

data class Event(
    val id: Int,
    val name: String,
    val active: Boolean,
    val start: Date,
    val duration: Duration,
    val zones: List<Zone>,
)
