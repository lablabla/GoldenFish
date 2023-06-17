package com.lablabla.goldenfish.presentation.edit_zone_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.presentation.navigation.GoldenFishScreenNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@GoldenFishScreenNavGraph
@Destination
@Composable
fun EditZoneScreen(
    zone: Zone,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Text(
            text = zone.name,
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier.height(4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun EditZoneScreenPreview() {
    EditZoneScreen(
        zone = Zone(0, "Zone 0", false),
        modifier = Modifier.background(MaterialTheme.colors.background)
    )
}
