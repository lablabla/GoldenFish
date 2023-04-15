package com.lablabla.goldenfish.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.lablabla.goldenfish.R
import com.lablabla.goldenfish.presentation.destinations.DirectionDestination
import com.lablabla.goldenfish.presentation.destinations.MainScreenDestination
import com.lablabla.goldenfish.presentation.destinations.ZonesScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestination,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(MainScreenDestination, Icons.Default.Home, R.string.home),
    Zones(ZonesScreenDestination, Icons.Default.Settings, R.string.zones),
//        Events(EventsScreenDestination, Icons.Default.Schedule, R.string.events)
}