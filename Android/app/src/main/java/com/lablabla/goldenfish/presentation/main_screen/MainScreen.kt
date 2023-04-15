package com.lablabla.goldenfish.presentation.main_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.lablabla.goldenfish.presentation.navigation.GoldenFishMainNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@SuppressLint("MissingPermission")
@GoldenFishMainNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    CheckBluetooth()
    CheckPermissions()
    viewModel.scanLeDevice(true)
    val state = viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        Button(
            onClick = { viewModel.scanLeDevice(true) }) {
            Text(text = "Refresh")
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            if (state.value.devices != null) {
                itemsIndexed(state.value.devices!!) { index, item ->
                    DeviceItem(
                        device = item
                    ) {
                        viewModel.connectToDevice(it)
                    }
                    if (index < state.value.devices!!.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            modifier = Modifier.height(4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(
    device: BluetoothDevice,
    onClick: (BluetoothDevice) -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 10.dp,
        modifier = Modifier.clickable {
            onClick(device)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = device.name,
                style = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = device.address,
                style = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions() {
    val locationPermissionsState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_START) {
                    locationPermissionsState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}

@Composable
fun CheckBluetooth() {

    val context = LocalContext.current
    val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode != Activity.RESULT_OK) {
            }
        }
    LaunchedEffect(key1 = true) {
        if (!bluetoothAdapter.isEnabled) {
            startForResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }
}