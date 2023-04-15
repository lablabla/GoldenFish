package com.lablabla.goldenfish.presentation.zones_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.lablabla.goldenfish.presentation.navigation.GoldenFishBottomNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@GoldenFishBottomNavGraph(start = true)
@Destination
@Composable
fun ZonesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Zones Screen",
            style = TextStyle(color = Color.Red)
        )
    }
}