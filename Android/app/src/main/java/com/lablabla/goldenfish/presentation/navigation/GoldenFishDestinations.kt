package com.lablabla.goldenfish.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.lablabla.goldenfish.R
import com.lablabla.goldenfish.presentation.destinations.DirectionDestination
import com.lablabla.goldenfish.presentation.destinations.SetupScreenDestination
import com.lablabla.goldenfish.presentation.destinations.StatusScreenDestination

enum class GoldenFishDestinations(
    val direction: DirectionDestination,
    val icon: ImageVector,
    @StringRes val label: Int) {

    Status(StatusScreenDestination, Icons.Default.Home, R.string.status),
    Setup(SetupScreenDestination, Icons.Default.Settings, R.string.setup),
}