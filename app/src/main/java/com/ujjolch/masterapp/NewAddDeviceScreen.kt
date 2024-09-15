package com.ujjolch.masterapp

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import com.example.masterapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Timer
import java.util.TimerTask

@Composable
fun NewAddDeviceScreen(bleScanViewModel: BleScanViewModel,
                       userDetailsViewModel: UserDetailsViewModel,
                       onNavigateToMyDevice:()->Unit,
                       backButtonEnabled:Boolean) {
    var showPrompt by remember { mutableStateOf(true) }
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
    val isScanning = remember { mutableStateOf(true) }
    val dataMutex = Mutex()
    LaunchedEffect(isScanning.value) {
        if (isScanning.value) {
            bleScanViewModel.startDiscovery()
        }
    }
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
            isScanning.value = false
            ScanningEnded = true
            isReScanEnabled = true
            if (finddevice != true) {
                showPrompt = false
            }
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
                            try {
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
                            } catch (e: Exception) {
                                Log.d("UJTAG45", "${e.message}")
                            }
                        }
                    }
                }, 0, 3000)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.AddDevice)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToMyDevice()},
                        enabled = backButtonEnabled) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if(backButtonEnabled) Color.Black else Color.White)
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 8.dp)
        },
        content = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .background(color = colorResource(id = R.color.Home_Screen_White))
    ) {
        if (showPrompt) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                RadarAnimation(isAnimating = isScanning.value)
                Spacer(modifier = Modifier.height(8.dp))
                Column(Modifier.padding(start = 24.dp)) {
                    Text(text = stringResource(id = R.string.AddDevicePrompt16))
                    Text(text = stringResource(id = R.string.AddDevicePrompt17))
                    Text(text = stringResource(id = R.string.AddDevicePrompt18))
                }
                Spacer(modifier = Modifier.height(22.dp))
                if (finddevice) {
                    devicesfound.value.forEach {
                        DeviceFoundRow(
                            deviceName = "Body Scale",
                            macAddress = it.address
                        ) {
                            userDetailsViewModel.bindDevice(
                                themistoscale(
                                    "Themisto body Scale",
                                    "${it.address}"
                                )
                            )
                            onNavigateToMyDevice()
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 85.dp)
            ) {
                Button(
                    onClick = {
                        finddevice = false
                        isReScanEnabled = false
                        bleScanViewModel.clearDataforRescan()
                        isScanning.value = true
                        bleScanViewModel.startDiscovery()
                        devicesfound.value -= devicesfound.value
                    },
                    enabled = isReScanEnabled,
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // Customize the background color
                        contentColor = Color.White
                    )
                ) {
                    androidx.compose.material3.Text(text = stringResource(id = R.string.AddDevicePrompt8))
                }
            }
        } else {
            Column(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
            ) {
                Column (Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                        Icon(painter = painterResource(id = R.drawable.nodevicefound),
                            modifier = Modifier.size(40.dp),
                            contentDescription = "ndf",
                            tint = colorResource(id = R.color.Icon_Tint))
                    Text(text = stringResource(id = R.string.AddDevicePrompt10), color = colorResource(id = R.color.Icon_Tint))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(Modifier.padding(horizontal = 24.dp)) {
                    Text(text = "⚠️ "+ stringResource(id = R.string.AddDevicePrompt10), fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = R.string.AddDevicePrompt11))
                    Text(text = stringResource(id = R.string.AddDevicePrompt12))
                    Text(text = stringResource(id = R.string.AddDevicePrompt13))
                    Text(text = stringResource(id = R.string.AddDevicePrompt14))
                    Text(text = stringResource(id = R.string.AddDevicePrompt15))
                }
            }
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 85.dp)
            ) {
                Button(
                    onClick = {
                        showPrompt = true
                        finddevice = false
                        isReScanEnabled = false
                        bleScanViewModel.clearDataforRescan()
                        isScanning.value = true
                        bleScanViewModel.startDiscovery()
                        devicesfound.value -= devicesfound.value
                    },
                    enabled = isReScanEnabled,
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // Customize the background color
                        contentColor = Color.White
                    )
                ) {
                    androidx.compose.material3.Text(text = stringResource(id = R.string.SearchAgain))
                }
            }
        }
    }
}
)
    
    
}

@Composable
fun DeviceFoundRow(deviceName: String, macAddress: String
                   ,onDeviceClick:()->Unit){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidthDp = configuration.screenWidthDp.dp
    Row (
        Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = deviceName,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Mac:$macAddress",
                fontWeight = FontWeight.Light)
        }
        Row (Modifier.padding(horizontal = 16.dp)){
        Button(
            modifier = if(screenWidthDp.value<600) Modifier.fillMaxWidth(0.7f) else Modifier.fillMaxWidth(0.3f),
            onClick = { onDeviceClick() }, shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Bind",
                color = Color.White
            )
        }
    }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDeviceFoundRow() {
//    DeviceFoundRow("Themisto body scale",
//        "00:1A:7D:DA:71:13",
//        {})
//}