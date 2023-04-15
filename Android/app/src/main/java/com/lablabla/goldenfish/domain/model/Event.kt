package com.lablabla.goldenfish.domain.model

data class Event(
    val id: Int,
    val name: String,
    val cron: String,
    val duration: Int,
    val zones: List<Zone>
)
