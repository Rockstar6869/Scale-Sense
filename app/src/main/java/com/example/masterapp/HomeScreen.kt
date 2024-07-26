package com.example.masterapp

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
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
fun HomeScreen(bleScanViewModel: BleScanViewModel = viewModel(),
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
    var NoComparableHist by remember { mutableStateOf(true) }
    var lastWeight by remember { mutableStateOf(0.0) }
    var lastWeightDate by remember { mutableStateOf("") }
    var secondLastWeight by remember { mutableStateOf(0.0) }
    var secondLastWeightDate by remember { mutableStateOf("") }
    var lastBMI by remember { mutableStateOf(0.0) }
    var secondLastBMI by remember { mutableStateOf(0.0) }
    lastBMI = Calculate.BMI(userdata?.heightincm?:0.0,lastWeight)
    secondLastBMI = Calculate.BMI(userdata?.heightincm?:0.0,secondLastWeight)
    var lastBodyFatPercent by remember { mutableStateOf(0.0) }
    var secondLastBodyFatPercent by remember { mutableStateOf(0.0) }
    var Increased by remember {mutableStateOf(false)}
    if(!userHistory.isNullOrEmpty()){
        val datehist = userHistory!!.map { it.date }
        val weighthist = userHistory!!.map { it.weight }

        lastWeight = weighthist.last()
        if(userHistory!!.size>1) {
            secondLastWeightDate = datehist[datehist.size - 2]
            secondLastWeight = weighthist[weighthist.size - 2]
            NoComparableHist = false
            if((lastWeight - secondLastWeight)>=0){
                Increased = true
            }
        }

        lastWeightDate = datehist.last()

    }
    if(userdata?.gender == "Male"){
        lastBodyFatPercent = Calculate.BodyFatPercentforMale(userdata!!.age,lastBMI)
        secondLastBodyFatPercent = Calculate.BodyFatPercentforMale(userdata!!.age,secondLastBMI)
    }
    else if (userdata?.gender == "Female"){
        lastBodyFatPercent = Calculate.BodyFatPercentforFemale(userdata!!.age,lastBMI)
        secondLastBodyFatPercent = Calculate.BodyFatPercentforFemale(userdata!!.age,secondLastBMI)
    }
    var showHealthReport by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val bluetoothEnabled = remember { mutableStateOf(isBluetoothEnabled(context)) }
    val locationEnabled = remember { mutableStateOf(isLocationEnabled(context)) }


    LaunchedEffect(!bluetoothEnabled.value,!locationEnabled.value) {
        while(!bluetoothEnabled.value || !locationEnabled.value) {
            bluetoothEnabled.value = isBluetoothEnabled(context)
            locationEnabled.value = isLocationEnabled(context)
            delay(1000)
        }
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
    LaunchedEffect(isScanning.value,bluetoothEnabled.value,locationEnabled.value) {
        if(isScanning.value&&bluetoothEnabled.value&&locationEnabled.value){
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
            try {
                updateddata.value.forEach {
                    if (it.address == MacAd.value) {
                        advertismentData = it.advertisementData
                    }
                }
            }
            catch (e:Exception){
                    Log.d("ujjTag45","${e}")
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
                        try{
                            for (device in updatedAvailableDevices) {
                                if (!devices.isNullOrEmpty()) {
                                    devices!!.forEach {
                                        if (device.address == it.address) {
                                            MacAd.value = it.address
                                            finddevice = true
                                        }
                                    }
                                }
                            }

                        }
                        catch(e:Exception){
                            Log.d("ujjTag45","${e}")
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
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .padding(end = 10.dp),
                    horizontalArrangement = Arrangement.End){
                    IconButton(onClick = { isRefreshing = true}
                        ,
                        Modifier
                            .background(color = Color.Black, shape = CircleShape)
                            .size(30.dp)) {
                        Icon(painter = painterResource(id = R.drawable.baseline_refresh_24),
                            contentDescription = "Refresh",
                            tint = Color.White)
                    }

                }
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
                                    if(lastWeight==0.0) {
                                        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                                        Text(
                                            text = "--",
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    else{
                                        Text(
                                            text = "$lastWeight",
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Column {
                                            Spacer(modifier = Modifier.padding(vertical = 6.dp))
                                            Text(
                                                text = "Kg",
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
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
                    .fillMaxSize()
            ){
                Column (
                    Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Spacer(modifier = Modifier.height(80.dp))
                    Box (
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(210.dp)){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = if(!NoComparableHist) lastWeightDate else "",
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if(!NoComparableHist) "${(lastWeight-secondLastWeight).format(2).toDouble()} Kg" else "--",
                                        fontWeight = FontWeight.Bold,
                                        modifier = if(!NoComparableHist) Modifier.padding(start = 18.dp) else Modifier,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        if(!NoComparableHist) {
                                            if (Increased) androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_increase),
                                                contentDescription = "increased",
                                                tint = Color.Green,
                                                modifier = Modifier.size(30.dp)
                                            )
                                            else androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_decrease),
                                                contentDescription = "decreased",
                                                tint = Color.Red,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }

                                        Text(
                                            text = "Weight",
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if(NoComparableHist) "--" else "${(lastBMI-secondLastBMI).format(2).toDouble()}",
                                        fontWeight = FontWeight.Bold,
                                        modifier = if(!NoComparableHist) Modifier.padding(start = 24.dp) else Modifier,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        if(!NoComparableHist) {
                                            if (Increased) androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_increase),
                                                contentDescription = "increased",
                                                tint = Color.Green,
                                                modifier = Modifier.size(30.dp)
                                            )
                                            else androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_decrease),
                                                contentDescription = "decreased",
                                                tint = Color.Red,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(
                                            text = "BMI",
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if(!NoComparableHist) "${(lastBodyFatPercent-secondLastBodyFatPercent).format(2).toDouble()}%" else "--",
                                        modifier = if(!NoComparableHist) Modifier.padding(start = 18.dp) else Modifier,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        if(!NoComparableHist) {
                                            if (Increased) androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_increase),
                                                contentDescription = "increased",
                                                tint = Color.Green,
                                                modifier = Modifier.size(30.dp)
                                            )
                                            else androidx.compose.material.Icon(
                                                painter = painterResource(id = R.drawable.baseline_decrease),
                                                contentDescription = "decreased",
                                                tint = Color.Red,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(
                                            text = "Body Fat(%)",
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = if(!NoComparableHist) "Compare with $secondLastWeightDate" else "",
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(25.dp))
                            Row (Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                                , verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = "More",
                                    fontSize = 15.sp,
                                    color = Color.Blue,
                                    modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    onNavigateToHealthReport()
                                })}
                                )
                                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription ="More",
                                        tint = Color.Blue,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .pointerInput(Unit) {
                                                detectTapGestures(onTap = {
                                                    if (!userHistory.isNullOrEmpty()) {
                                                        onNavigateToHealthReport()
                                                    }
                                                })
                                            }
                                    )

                            }
                        }


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
                    Icon(painter = painterResource(id = R.drawable.baseline_monitor_weight_24),
                        contentDescription = "weight"
                            ,Modifier.align(Alignment.CenterHorizontally)
                            .padding(end = 12.dp))

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
                            modifier = Modifier.padding(vertical = 8.dp),
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
                    Icon(painter = painterResource(id = R.drawable.calculator),
                        contentDescription = "BMI"
                        ,Modifier.align(Alignment.CenterHorizontally))
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
                            modifier = Modifier.padding(vertical = 8.dp),
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
                    Icon(painter = painterResource(id = R.drawable.google_fit),
                        contentDescription = "BodyFat"
                        ,Modifier.align(Alignment.CenterHorizontally)
                            .padding(end = 28.dp))

                    if(Locked!="1") {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "--",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 4.dp),
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
                                modifier = Modifier.padding(end = 15.dp)
                                    .padding(vertical = 8.dp),
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
                                modifier = Modifier.padding(end = 15.dp)
                                    .padding(vertical = 8.dp),
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