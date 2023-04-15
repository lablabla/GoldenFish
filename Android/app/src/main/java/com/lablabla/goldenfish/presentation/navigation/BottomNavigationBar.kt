package com.lablabla.goldenfish.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lablabla.goldenfish.presentation.NavGraphs
import com.lablabla.goldenfish.presentation.appCurrentDestinationAsState
import com.lablabla.goldenfish.presentation.destinations.Destination
import com.lablabla.goldenfish.presentation.startAppDestination
import com.ramcosta.composedestinations.navigation.navigateTo


@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    val currentDestination: Destination? = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.goldenFishBottom.startAppDestination
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        BottomBarDestination.values().forEach { destination ->
            val selected = currentDestination == destination.direction
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigateTo(destination.direction) {
                        launchSingleTop = true
                    }
                },

                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.label)
                        )
                        if(selected) {
                            Text(
                                text = stringResource(destination.label),
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}