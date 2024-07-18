package com.example.masterapp

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs

@Composable
fun HomeScreen(bleScanViewModel: BleScanViewModel,
               userDetailsViewModel: UserDetailsViewModel = viewModel()
                ,onNavigateToHealthReport:()->Unit){
    var progress by remember {
        mutableStateOf(false)
    }
    val availableDevices by bleScanViewModel.availableDevices.observeAsState(emptyList())
//    val MacAd = "D8:E7:2F:CA:C6:EA"
    val MacAd = remember {
        mutableStateOf("")
    }
    val data by bleScanViewModel.data.observeAsState()
    val updateddata = remember {
        mutableStateOf<Set<BleDevice>>(emptySet())
    }
    var finddevice by remember { mutableStateOf(false) }
    var findmydeviceclick by remember { mutableStateOf(false) }
    var updatedAvailableDevices by remember { mutableStateOf(emptyList<BluetoothDevice>()) }
    val isScanning = remember { mutableStateOf(true) }
    val isAvailable = remember { mutableStateOf(false) }
    var Locked by remember { mutableStateOf("") }
    var DecimalPoint by remember { mutableStateOf("") }
    var Unit by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf(0.0) }
    var weightinkgs by remember { mutableStateOf(0.0) }
    var impedence by remember { mutableStateOf(0) }
    var  messagebodyproperties by remember { mutableStateOf("") }
    var advertismentData by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val devices by userDetailsViewModel.devices.observeAsState()
    val userdata by userDetailsViewModel.userData.observeAsState()
    val userHistory by userDetailsViewModel.userHist.observeAsState()
    var lastWeight by remember { mutableStateOf(0.0) }
    if(!userHistory.isNullOrEmpty()){
        val weighthist = userHistory!!.map { it.weight }
        lastWeight = weighthist.last()
    }
    var showHealthReport by remember {
        mutableStateOf(false)
    }

    if (finddevice) {
        updateddata.value.forEach {
            if (it.address == MacAd.value) {
                messagebodyproperties = it.advertisementData.subSequence(30,32).toString()
                Locked = getDataType(messagebodyproperties).toString()
                DecimalPoint = getDecimalPoint(messagebodyproperties)
                Unit = getUnit(messagebodyproperties)
                if(Locked == "1"){
                    isScanning.value = false
                    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                        Date()
                    )
                    if(lastWeight==0.0) {
                        userDetailsViewModel.updatehistlist(
                            hist(
                                weightinkgs,
                                impedence,
                                currentDate
                            )
                        )
                        showHealthReport = true
                    }
                    else if(lastWeight!=0.0 && abs(weightinkgs-lastWeight)<3.0){
                        userDetailsViewModel.updatehistlist(
                            hist(
                                weightinkgs,
                                impedence,
                                currentDate
                            )
                        )
                        showHealthReport = true
                    }
                    else if(lastWeight!=0.0 && abs(weightinkgs-lastWeight)>3.0){
                        AlertBox(
                            difference = (weightinkgs - lastWeight),
                            onYesClick = {
                                userDetailsViewModel.updatehistlist(
                                    hist(
                                        weightinkgs,
                                        impedence,
                                        currentDate
                                    )
                                )
                                showHealthReport = true
                            })
                    }
                }
                weight = addDecimalPoint2(
                    binarydecimalpoint = DecimalPoint,
                    number = hexToDecimal(
                        ((it.advertisementData.subSequence(12, 17)).toString())
                            .replace("\\s".toRegex(), "")
                    )

                )
                if(Unit == "00"){
                    weightinkgs = weight
                }
                else if(Unit =="10"){
                    weightinkgs = Calculate.convertpoundsToKilograms(weight)
                }
                impedence =
                    hexToDecimal(
                        ((it.advertisementData.subSequence(18, 23)).toString())
                            .replace("\\s".toRegex(), "")
                    )    }}}


    fun updatelist(){
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
            }, 0, 1000) // Update every 3 seconds
        }
        onDispose {
            timer.cancel()
        }
    }

    fun updateData() {
        data?.let { devices ->
            updateddata.value = updateddata.value - updateddata.value
            updateddata.value = devices
//            Log.d("UJC","${updateddata.value}")
        }
    }
    LaunchedEffect(isScanning.value) {
        if(isScanning.value){
            bleScanViewModel.startDiscovery()
        }
    }
    LaunchedEffect(isScanning.value) {
        if(isScanning.value) {
            while (true) {
                updateData()
                delay(100)
            }
        }
    }

    LaunchedEffect (finddevice){
        if(finddevice){
            updateddata.value.forEach {
                if (it.address == MacAd.value) {
                    advertismentData = it.advertisementData
                }
            }
        }
    }

//    LaunchedEffect(isScanning.value) {
//        if (!finddevice) {
//            val timer = Timer()
//            if (isScanning.value) {
//                timer.schedule(object : TimerTask() {
//                    override fun run() {
//                        for (device in updatedAvailableDevices) {
//                            if (device.address == MacAd) {
//                                finddevice = true
//                                break
//                            }
//                        }
//                    }
//                }, 0, 1000)
//            }
//        }
//    }

    LaunchedEffect(isScanning.value) {
        if (!finddevice) {
            val timer = Timer()
            if (isScanning.value) {
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        for (device in updatedAvailableDevices) {
                            if(!devices.isNullOrEmpty()){
                                devices!!.forEach {
                                    if (device.address == it.address) {
                                        MacAd.value = it.address
                                        finddevice = true
                            }
                                }
                            }
                        }

                    }
                }, 0, 500)
            }
        }
    }


    LaunchedEffect(isScanning.value) {
        if (isScanning.value) {
            delay(40000)
            bleScanViewModel.stopDiscovery()
            isScanning.value=false
        }
    }

    LaunchedEffect(isRefreshing) {
        finddevice=false
        bleScanViewModel.clearDataforRescan()
        bleScanViewModel.clearAvailableDevicesforRescan()
        isScanning.value=true
        delay(2000)
        isRefreshing = false
    }


    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
        },
    ) {
        Box (
            Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.Home_Screen_White))){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top half blue
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxSize()
                    .background(
                        colorResource(id = R.color.Home_Screen_Blue),
                        shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                    )
            ){
                Column (horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()){
                    Spacer(modifier = Modifier.padding(vertical = 40.dp))
                    RotatingCustomProgressIndicator(progress = 0.5f, isRotating = if(Locked == "0" && finddevice) true else false,
                        textContent = {
                            Column {
                                if(messagebodyproperties=="" || !finddevice){
                                    Text(text = "Unconnected",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White)
                                }
                                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                                Row{
                                if(finddevice) {
                                    Text(
                                        text = "$weight",
                                        fontSize = 45.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Column {
                                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                        Text(
                                            text = if (Unit =="00") "Kg" else "LB",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                else{
                                    Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                                    Text(
                                        text = "--",
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                }
                            }
                        })
                }
            }

            // Bottom half
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Column (Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
//                    Text(text = advertismentData)
//                        Text(text ="$lastWeight")
//                    CustomSlider3(value = 56.7f, onValueChange = {},
//                        blueRange = 0f..50.0f,
//                        greenRange = 50.0f..100f,
//                        orangeRange = 100f..150f,
//                        blueText = "Low",
//                        greenText = "Stan",
//                        orangeText = "High")
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 200.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        Text(text = "My Health Report",
                            color =  colorResource(id = R.color.Slider_DarkGreen),
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    onNavigateToHealthReport()
                                })})
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Health Report",
                            tint = colorResource(id = R.color.Slider_DarkGreen),
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    onNavigateToHealthReport()
                                })})
                    }

                }


            }
        }}}
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 350.dp)
            .fillMaxWidth()
            .height(250.dp), // Adjust the height as needed
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(Locked!="1") {
                        Row {
                            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                            Text(
                                text = "--",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 30.sp
                            )
                        }
                    }
                    else{
                        Text(
                            text = "$weightinkgs Kg",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Weight",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                VerticalDivider()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(Locked!="1") {
                        Text(
                            text = "--",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 30.sp
                        )
                    }
                    else{
                        Text(
                            text = "${Calculate.BMI(userdata?.heightincm?:0.0,weightinkgs)}",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "BMI",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                VerticalDivider()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 18.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(Locked!="1") {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "--",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 30.sp
                            )
                        }
                    }
                    else{
                        if(userdata?.gender == "Male") {
                            Text(
                                text = "${
                                    Calculate.BodyFatPercentforMale(
                                        userdata?.age ?: 0,
                                        Calculate.BMI(userdata?.heightincm ?: 0.0, weightinkgs)
                                    )
                                }%",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                        else{
                            Text(
                                text = "${
                                    Calculate.BodyFatPercentforFemale(
                                        userdata?.age ?: 0,
                                        Calculate.BMI(userdata?.heightincm ?: 0.0, weight)
                                    )
                                }%",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Body Fat(%)",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }}
