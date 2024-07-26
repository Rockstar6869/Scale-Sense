package com.example.masterapp

import android.bluetooth.BluetoothDevice
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Timer
import java.util.TimerTask

@Composable
fun AddDevice(bleScanViewModel: BleScanViewModel = viewModel(),
              userDetailsViewModel: UserDetailsViewModel = viewModel(),
              onNavigateToMainView:() ->Unit
) {
    var showPrompt by remember { mutableStateOf(false) }
    val availableDevices by bleScanViewModel.availableDevices.observeAsState(emptyList())
    val data by bleScanViewModel.data.observeAsState()
    val MACAD = remember { mutableStateOf("") }
    var finddevice by remember { mutableStateOf(false) }
    var ScanningEnded by remember { mutableStateOf(false) }
    var findmydeviceclick by remember { mutableStateOf(false) }
    var isReScanEnabled by remember {
        mutableStateOf(false)
    }
    val updateddata = remember {
        mutableStateOf<Set<BleDevice>>(emptySet())
    }
    val devicesfound = remember {
        mutableStateOf<Set<BleDevice>>(emptySet())
    }
    var updatedAvailableDevices by remember { mutableStateOf(emptyList<BluetoothDevice>()) }
    val isScanning = remember { mutableStateOf(false) }
    val dataMutex = Mutex()
    suspend fun updateData() {
        data?.let { devices ->
            dataMutex.withLock {
                updateddata.value = updateddata.value - updateddata.value
                updateddata.value = devices
            }
        }
    }

    LaunchedEffect(isScanning.value) {
        if (isScanning.value) {
           delay(20000)
            bleScanViewModel.stopDiscovery()
            isScanning.value=false
            showPrompt = false
            ScanningEnded =true
            isReScanEnabled= true
        }
    }
    
    LaunchedEffect(isScanning.value) {
        if (isScanning.value) {
            while (true) {
                updateData()
                delay(100)
            }
        }
    }
    fun updatelist() {
        updatedAvailableDevices = availableDevices
//        Log.e("UJC3","${updatedAvailableDevices.toList()}")
    }
    DisposableEffect(isScanning.value) {
        val timer = Timer()
        if (isScanning.value) {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    updatelist()
                }
            }, 0, 1000) // Update every 1 seconds
        }
        onDispose {
            timer.cancel()
        }
    }

    LaunchedEffect(isScanning.value) {
//        if (!finddevice) {
//            for (device in updatedAvailableDevices) {
//                Log.e("UJC2","${device}")
//                if (device.address == MacAd) {
//                    finddevice = true
//                    break
//                }
//            }
//        }
        if (!finddevice) {
            val timer = Timer()
            if (isScanning.value) {
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        CoroutineScope(Dispatchers.Default).launch {
                            try{
                            dataMutex.withLock {
                                for (device in updateddata.value) {
                                    if (device.advertisementData.startsWith("10 FF")) {
                                        finddevice = true
                                        devicesfound.value += BleDevice(
                                            device.name,
                                            device.address,
                                            ""
                                        )
                                    }
                                }
                            }
                        } catch (e:Exception){
                                Log.d("UJTAG45","${e.message}")
                            }
                    }
                    }
                }, 0, 3000)
            }
        }
    }

//    Column() {
//        Button(onClick = {
//            bleScanViewModel.startDiscovery()
//            findmydeviceclick = true
//            isScanning.value = true
//        }) {
//            Text(text = "Add Devices")
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFF0F0F0)), // Light background color
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(800.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Before searching for the device, please make sure:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " Bluetooth",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " is turned on",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " is enabled",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                if(!finddevice) {
                    Button(
                        onClick = {
                            showPrompt = true
                            bleScanViewModel.startDiscovery()
                            findmydeviceclick = true
                            ScanningEnded=false
                            isScanning.value = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Customize the background color
                            contentColor = Color.White)
                    ) {
                        Text(text = "Find my Scale", color = Color.White)
                    }
                }
                if (showPrompt && !finddevice) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "We are searching for your device. Please make sure your scale is on...",
                        fontSize = 16.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                    LinearProgressIndicator()
                }
                else if(finddevice){
                    Text(text = "We Found These Devices",
                            fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center)
                    Text(text = "Tap on your Device to Bind it",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    devicesfound.value.forEach {
                        Spacer(modifier = Modifier.height(8.dp))
                        DeviceInfoCard(deviceName = it.name, macAddress = it.address,
                            onDeviceClick = {userDetailsViewModel.bindDevice(themistoscale("Themisto body Scale","${it.address}"))
                            onNavigateToMainView()})
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    Button(onClick = { finddevice=false
                                     showPrompt=true
                                        isReScanEnabled=false
                                        bleScanViewModel.clearDataforRescan()
                                        isScanning.value=true
                                     bleScanViewModel.startDiscovery()
                                     devicesfound.value -= devicesfound.value},
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Customize the background color
                            contentColor = Color.White),
                        enabled = isReScanEnabled) {
                        Text(text = "Rescan")
                    }
                    Spacer(modifier = Modifier.padding(vertical = 70.dp))
                    if(!isReScanEnabled){
                    LinearProgressIndicator()}


                }  
                else if(ScanningEnded && devicesfound.value.isEmpty()){
                    Spacer(modifier = Modifier.padding(vertical = 25.dp))
                    Text(text = "We could not find any device! Make sure your scale is on and try again",
                            fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun DeviceInfoCard(deviceName: String, macAddress: String
    ,onDeviceClick:()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeviceClick()},
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(text = "Device Name: ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    color = Color(0xFF333333))
                Text(
                    text = if(deviceName=="No Name") "Themisto Body Scale" else deviceName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF333333)
                )
            }
            Row {
                Text(
                    text = "Mac Adress: ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    color = Color(0xFF333333)
                )
                Text(
                    text = macAddress,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}