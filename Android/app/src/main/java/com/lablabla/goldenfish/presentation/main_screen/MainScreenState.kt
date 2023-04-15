package com.lablabla.goldenfish.presentation.main_screen

import android.bluetooth.BluetoothDevice


data class MainScreenState(
    val devices: List<BluetoothDevice>? = null
)