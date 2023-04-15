package com.lablabla.goldenfish.presentation.main_screen

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import blufi.espressif.BlufiCallback
import blufi.espressif.BlufiClient
import blufi.espressif.params.BlufiParameter
import com.lablabla.goldenfish.Secrets
import com.lablabla.goldenfish.data.model.MQTTConfigMessage
import com.lablabla.goldenfish.data.model.Message
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val app: Application,
): ViewModel() {

    private var _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    private var bluetoothLeScanner: BluetoothLeScanner
    private val scanCallback = DeviceScanCallback()

    @Inject
    lateinit var jsonMessageAdapter: JsonAdapter<Message>

    init {
        val bluetoothManager = app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if (!adapter.isEnabled) {
            // TODO: Move to next screen if device exists, else notify screen to request again/exit
        }
        bluetoothLeScanner = adapter.bluetoothLeScanner
    }

    fun scanLeDevice(start: Boolean) {
        if (start) {
            val filter = ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(BlufiParameter.UUID_SERVICE))
                .build()
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build()
            bluetoothLeScanner.startScan(listOf(filter), settings, scanCallback)
            Timber.d("Started scanning")
        } else {
            bluetoothLeScanner.stopScan(scanCallback)
            Timber.d("Stopped scanning")
        }
    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice) {
        Timber.d("Trying to connect to device ${bluetoothDevice.name} at address ${bluetoothDevice.address}")

        val blufiClient = BlufiClient(app.applicationContext, bluetoothDevice)
        blufiClient.setGattCallback(object: BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                val devAddr = gatt!!.device.address
                Timber.d(
                    String.format(
                        Locale.ENGLISH, "onConnectionStateChange addr=%s, status=%d, newState=%d",
                        devAddr, status, newState
                    )
                )
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED -> {
                            Timber.d(String.format("Connected %s", devAddr))
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                            gatt.close()
                            Timber.d(String.format("Disconnected %s", devAddr))
                        }
                    }
                } else {
                    gatt.close()
                    Timber.d(
                        String.format(Locale.ENGLISH, "Disconnect %s, status=%d", devAddr, status)
                    )
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

                Timber.d(String.format(Locale.ENGLISH, "onServicesDiscovered status=%d", status))
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    gatt!!.disconnect()
                    Timber.d(
                        String.format(
                            Locale.ENGLISH,
                            "Discover services error status %d",
                            status
                        )
                    )
                }
                else {
                    blufiClient.negotiateSecurity()
                }
            }
        })

        blufiClient.setBlufiCallback(object: BlufiCallback() {
            override fun onGattPrepared(
                client: BlufiClient?,
                gatt: BluetoothGatt?,
                service: BluetoothGattService?,
                writeChar: BluetoothGattCharacteristic?,
                notifyChar: BluetoothGattCharacteristic?
            ) {
                if (service == null) {
                    Timber.w("Discover service failed")
                    gatt!!.disconnect()
                    return
                }
                if (writeChar == null) {
                    Timber.w("Get write characteristic failed")
                    gatt!!.disconnect()
                    return
                }
                if (notifyChar == null) {
                    Timber.w("Get notification characteristic failed")
                    gatt!!.disconnect()
                    return
                }

                val mtu = 512
                Timber.d("Request MTU $mtu")
                val requestMtu = gatt!!.requestMtu(mtu)
                if (!requestMtu) {
                    Timber.w("Request mtu failed")
                }
            }

            override fun onNegotiateSecurityResult(client: BlufiClient?, status: Int) {
                if (status == STATUS_SUCCESS) {
                    Timber.d("Negotiate security complete")
                    client?.let {
                        val secrets = Secrets()
                        val mqttConfig = MQTTConfigMessage(
                            brokerAddress = secrets.getBrokerAddress(app.packageName),
                            userName = secrets.getBrokerUserName(app.packageName),
                            password = secrets.getBrokerUPassword(app.packageName)
                        )
                        val json = jsonMessageAdapter.toJson(mqttConfig)
                        client.postCustomData(json.toByteArray())
                    }
                } else {
                    Timber.d("Negotiate security failed， code=$status")
                }
            }
        })
        blufiClient.setGattWriteTimeout(5000L)
        blufiClient.connect()
    }

    private inner class DeviceScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Timber.d("onScanResult: ")
            result?.let {scanResult ->
                Timber.d("Found device ${scanResult.device.name}, Address ${scanResult.device.address}")
                _state.update {
                    it.copy(
                        devices = listOf(scanResult.device, scanResult.device)
                    )
                }
            }
            scanLeDevice(false)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Timber.d("onBatchScanResults with ${results?.size} results")
            results?.let {
                _state.update {
                    it.copy(
                        devices = it.devices
                    )
                }
            }
            scanLeDevice(false)
        }
    }
}