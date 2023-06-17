package com.lablabla.goldenfish.presentation.status_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lablabla.goldenfish.R
import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.presentation.component.ExpandableCard

@Composable
fun ZoneItem(zone: Zone) {
    ExpandableCard(
        title = zone.name
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Next run: ",
                style = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.height(6.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.quickstart),
                        contentDescription = "Quick start"
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.time_start),
                        contentDescription = "Start for duration"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ZoneItemPreview() {
    ZoneItem(zone = Zone(0, "Zone 0", false))
}