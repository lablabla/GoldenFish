package com.lablabla.goldenfish.presentation.status_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.presentation.component.ExpandableCard
import com.lablabla.goldenfish.presentation.navigation.GoldenFishScreenNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@GoldenFishScreenNavGraph(start = true)
@Destination
@Composable
fun StatusScreen(
//    device: Device,
    modifier: Modifier = Modifier,
    zones: List<Zone> = listOf(Zone(
        0,
        "Zone 0",
        false
    ))
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colors.background),
    ) {
        itemsIndexed(zones) {index, item ->
            ZoneItem(zone = item)
            if (index < zones.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.height(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun StatusScreenPreview() {
    StatusScreen(zones =
        listOf(
            Zone(
                0,
                "Zone 0",
                false
            ),
            Zone(
                1,
        "Zone 1",
        false
            ),
            Zone(
                2,
        "Zone 2",
        false
            )
        )
    )
}