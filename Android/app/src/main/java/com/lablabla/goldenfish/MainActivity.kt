package com.lablabla.goldenfish

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lablabla.goldenfish.presentation.NavGraphs
import com.lablabla.goldenfish.presentation.navigation.BottomNavigationBar
import com.lablabla.goldenfish.ui.theme.GoldenFishTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoldenFishTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.goldenFishMain
                    )
                }
            }
        }
    }
}