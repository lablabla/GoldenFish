package com.lablabla.goldenfish.domain.model

data class MQTTConfig(
    val brokerAddress: String,
    val userName: String,
    val password: String
)
