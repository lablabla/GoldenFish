package com.lablabla.goldenfish.data.model

import com.squareup.moshi.JsonClass

enum class Opcode {
    MQTT_CONFIG
}
sealed class Message(
    val opcode: Opcode
)

@JsonClass(generateAdapter = true)
data class MQTTConfigMessage(
    val brokerAddress: String,
    val userName: String,
    val password: String
): Message(Opcode.MQTT_CONFIG)

