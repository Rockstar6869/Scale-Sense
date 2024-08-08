package com.example.masterapp

import android.nfc.Tag
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull

@Composable
fun HealthReportScreen(onBackClick: () -> Unit,
                       userDetailsViewModel: UserDetailsViewModel = viewModel()) {

    val homeScreenBlue by remember {
        mutableStateOf( R.color.Home_Screen_Blue)
    }
    val homeScreenWhite by remember {
        mutableStateOf(R.color.Home_Screen_White)
    }

    val userData by userDetailsViewModel.userData.observeAsState()
    val userHistory by userDetailsViewModel.userHist.observeAsState()
    var lastWeight by remember { mutableStateOf(0.0) }
    var lastImpedance by remember { mutableStateOf(0) }
    var secondLastWeight by remember { mutableStateOf(0.0) }
    var secondLastWeightDate by remember { mutableStateOf("") }
    val userUnits by userDetailsViewModel.units.observeAsState()
    var userUnitLastWeight by remember {
        mutableStateOf("")
    }
    LaunchedEffect(userUnits,lastWeight) {
        if(userUnits.isNotNull()){
            if(userUnits!!.weightunit == "kg") {
                userUnitLastWeight = "$lastWeight Kgs"
            }
            else if(userUnits!!.weightunit == "lb"){
                userUnitLastWeight = "${Calculate.convertKgToPounds(lastWeight).format(2)} lbs"
            }
        }
        else{  //When user has not selected any Unit
            userUnitLastWeight = "$lastWeight Kgs"
        }
    }

    if(!userHistory.isNullOrEmpty()){
        val datehist = userHistory!!.map { it.date }
        val weighthist = userHistory!!.map { it.weight }
        lastWeight = weighthist.last()


        if(userHistory!!.size>1) {
            secondLastWeight = weighthist[weighthist.size - 2]
            secondLastWeightDate = datehist[datehist.size - 2]
        }

        val impedancehist = userHistory!!.map { it.impedance }
        lastImpedance = impedancehist.last()
    }

    var BMI by remember { mutableStateOf(0.0) }
    var bodyWater by remember { mutableStateOf(0.0) }
    var bodyWaterPercent by remember { mutableStateOf(0.0) }
    var bodyFatPercent by remember { mutableStateOf(0.0) }
    var LeanBodyMass by remember { mutableStateOf(0.0) }
    var LeanBodyMassPercent by remember { mutableStateOf(0.0) }
    var BMR by remember { mutableStateOf(0.0) }
    var secondLastBodyFatPercent by remember { mutableStateOf(0.0) }
    var secondLastBMI by remember { mutableStateOf(0.0) }
    BMI = Calculate.BMI(userData?.heightincm?:0.0,lastWeight)
    if(userData?.gender == "Male"){
        bodyWater = Calculate.BodyWaterForMale(userData!!.age,userData!!.heightincm,lastWeight)
        bodyFatPercent = Calculate.BodyFatPercentforMale(userData!!.age,BMI)
        LeanBodyMass = Calculate.LeanBodyMass(lastWeight,bodyFatPercent)
        LeanBodyMassPercent = Calculate.LeanBodyMassPercent(LeanBodyMass,lastWeight)
        BMR = Calculate.BMRforMale(lastWeight,userData!!.heightincm,userData!!.age)

        secondLastBodyFatPercent = Calculate.BodyFatPercentforMale(userData!!.age,secondLastBMI)
    }
    else if(userData?.gender == "Female"){
        bodyWater = Calculate.BodyWaterForFemale(userData!!.age,userData!!.heightincm,lastWeight)
        bodyFatPercent = Calculate.BodyFatPercentforFemale(userData!!.age,BMI)
        LeanBodyMass = Calculate.LeanBodyMass(lastWeight,bodyFatPercent)
        LeanBodyMassPercent = Calculate.LeanBodyMassPercent(LeanBodyMass,lastWeight)
        BMR = Calculate.BMRforFemale(lastWeight,userData!!.heightincm,userData!!.age)

        secondLastBodyFatPercent = Calculate.BodyFatPercentforFemale(userData!!.age,secondLastBMI)
    }
    bodyWaterPercent = Calculate.BodyWaterPercent(bodyWater,lastWeight)

    secondLastBMI = Calculate.BMI(userData?.heightincm?:0.0,secondLastWeight)

    LaunchedEffect (true){
        userDetailsViewModel.gethistlist()
    }

        Scaffold(
            backgroundColor = colorResource(id = homeScreenWhite),
            topBar = {
                TopAppBar(
                    title = {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 70.dp)
                                .padding(top = 40.dp)
                        , horizontalArrangement = Arrangement.Center){
                            Text(text = "Health Report",
                                color = Color.White)
                        }
                            },
                    navigationIcon = {
                        IconButton(onClick = onBackClick,
                            Modifier.padding(top = 40.dp)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    backgroundColor = colorResource(id = homeScreenBlue),
                    contentColor = Color.Black,
                    modifier = Modifier
                        .height(100.dp),
                    elevation = 0.dp
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
//                    item{
//                        InfoCard(title = "Compare with $secondLastWeightDate",
//                            weight = "${(lastWeight-secondLastWeight).format(2).toDouble()} Kg",
//                            BMI = "${(BMI-secondLastBMI).format(2).toDouble()}",
//                            BodyFatPercent = "${(bodyFatPercent-secondLastBodyFatPercent).format(2).toDouble()}%"
//                                , Increased = if(lastWeight-secondLastWeight>0) true else false)
//                    }
                    item{
                        BodyInfoBox()
                        Column (
                            Modifier
                                .background(Color.White)){
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                Modifier.padding(start = 16.dp),
                            ) {
                                Text(text = "Body Composition",
                                    color = colorResource(id = homeScreenBlue))

                            }
                            Spacer(modifier = Modifier.height(20.dp))
                                Divider(
                                    color = Color.Blue,
                                    thickness = (0.9).dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                        }

//                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = Color.White)) {
                            Column{
                            ExpandableRow(index = "Weight",
                                value = "$userUnitLastWeight",
                                Tag = {
                                    if(lastWeight<=40){
                                        BlueTag(text = "Underweight")
                                    }
                                    else if(lastWeight>40 && lastWeight<=80){
                                        GreenTag(text = "Normal")
                                    }
                                    else{
                                        OrangeTag(text = "Overweight")
                                    }
                                }) {
                                    CustomSlider3(
                                        value = lastWeight.toFloat(),
                                        onValueChange = {},
                                        blueRange = 0f..40f,
                                        greenRange = 40f..80f,
                                        orangeRange = 80f..120f,
                                        blueText = "Underweight",
                                        greenText = "Normal",
                                        orangeText = "Overweight",
                                        trackHeight = 20f
                                    )
                            }
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                thickness = 1.dp, // Very thin thickness
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ExpandableRow(index = "BMI",
                                value = "$BMI",
                                Tag = {
                                    if(BMI<=18.5){
                                        BlueTag(text = "Underweight")

                                    }        
                                    else if(BMI>18.5 && BMI<25){
                                        GreenTag(text = "Normal")

                                    }   
                                    else if(BMI>=25 && BMI<30){
                                        OrangeTag(text = "High")
                                    }
                                    else{
                                        RedTag(text = "Overweight")
                                    }
                                }) {
                                BMISlider(
                                    value = BMI.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..18.5f,
                                    greenRange = 18.5f..25.0f,
                                    orangeRange = 25.0f..30.0f,
                                    redRange = 30.0f..35f,
                                    blueText = "Underweight",
                                    greenText = "Normal",
                                    orangeText = "High",
                                    redText = "Overweight",
                                    trackHeight = 20f
                                )
                            }
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                thickness = 1.dp, // Very thin thickness
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ExpandableRow(index = "Body Water%",
                                value = "$bodyWaterPercent%",
                                Tag = {
                                    if(bodyWaterPercent<=50){
                                        BlueTag(text = "Low")

                                    }
                                    else if(bodyWaterPercent>50 && bodyWaterPercent<=65){
                                        GreenTag(text = "Standard")
                                    }
                                    else{
                                        OrangeTag(text = "High")
                                    }
                                }) {
                                BodyWaterPercentSlider(
                                    value = bodyWaterPercent.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..50f,
                                    greenRange = 50f..65f,
                                    orangeRange = 65f..115f,
                                    blueText = "Low",
                                    greenText = "Standard",
                                    orangeText = "High",
                                    trackHeight = 20f
                                )
                            }
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                thickness = 1.dp, // Very thin thickness
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ExpandableRow(index = "Body Fat%",
                                value = "$bodyFatPercent%",
                                Tag = {
                                    if(bodyFatPercent<=6){
                                        BlueTag(text = "Low")
                                    }
                                    else if(bodyFatPercent>6 && bodyFatPercent<=22){
                                        GreenTag(text = "Standard")
                                    }
                                    else if(bodyFatPercent>22 && bodyFatPercent<=27){
                                        OrangeTag(text = "High")

                                    }
                                    else{
                                        RedTag(text = "Over")
                                    }
                                }) {
                                BodyFatPercentSlider(
                                    value = bodyFatPercent.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..6f,
                                    greenRange = 6f..22f,
                                    orangeRange = 22f..27f,
                                    redRange = 27f..38f,
                                    blueText = "Low",
                                    greenText = "Standard",
                                    orangeText = "High",
                                    redText = "Over",
                                    trackHeight = 20f
                                )
                            }
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                thickness = 1.dp, // Very thin thickness
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            ExpandableRow(
                                index = "Lean Body Mass%",
                                value = "$LeanBodyMassPercent%",
                                Tag = {
                                    if(LeanBodyMassPercent<68){
                                        BlueTag(text = "Low")
                                    }
                                    else if(LeanBodyMassPercent>=68 && LeanBodyMassPercent<=90){
                                        GreenTag(text = "Standard")
                                    }
                                    else{
                                        OrangeTag(text = "High")
                                    }
                                }
                            ) {
                                LeanBodyMassPercentSlider(
                                    value = LeanBodyMassPercent.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..68f,
                                    greenRange = 68f..90f,
                                    orangeRange = 90f..100f,
                                    blueText = "Low",
                                    greenText = "Standard",
                                    orangeText = "High",
                                    trackHeight = 20f
                                )
                            }
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                thickness = 1.dp, // Very thin thickness
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ExpandableRow(index = "BMR",
                                value = "$BMR kcal",
                                Tag = {
                                    if(BMR<=1450){
                                        BlueTag(text = "Low")
                                    }
                                    else if(BMR>1450 && BMR<=1650){
                                        GreenTag(text = "Good")

                                    }
                                    else if(BMR>1650){
                                        DarkGreenTag(text = "Excellent")

                                    }                                }) {
                                BMRSlider(
                                    value = BMR.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..1450f,
                                    greenRange = 1450f..1650f,
                                    darkGreenRange = 1650f..3000f,
                                    blueText = "Low",
                                    greenText = "Good",
                                    darkGreenText = "Excellent",
                                    trackHeight = 20f

                                )
                            }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Bone%",
                                    value = "0%",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Protein%",
                                    value = "0%",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Muscle%",
                                    value = "0%",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Visceral Fat Index",
                                    value = "0",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Subcutaneous Fat",
                                    value = "0 Kg",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "Body Age",
                                    value = "0",
                                    Tag = {}) {
                                    //Slider
                                }
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ExpandableRow(index = "AMR",
                                    value = "0 Kcal",
                                    Tag = {}) {
                                    //Slider
                                }

                        }
                    }
                        Spacer(modifier = Modifier.height(15.dp))

                        Column (
                            Modifier
                                .background(Color.White)){
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                Modifier.padding(start = 16.dp),
                            ) {
                                Text(text = "Weight Management",
                                    color = colorResource(id = homeScreenBlue))

                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(
                                color = Color.Blue,
                                thickness = (0.9).dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }


                        
//                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = Color.White)) {
                            Column {
                                NonExpandableRow(index = "Standard weight", value = "0.00 Kg")
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                NonExpandableRow(index = "Weight control", value = "0.00 Kg")
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                NonExpandableRow(index = "Fat control", value = "0.00 Kg")
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                NonExpandableRow(index = "Muscle control", value = "0.00 Kg")
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )

                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }

                        }

                    }

                })

    }

@Composable
fun ExpandableRow(index: String, value: String,
                  Tag:@Composable () -> Unit,
                  expandedContent: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    isExpanded = !isExpanded
                })
            }) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = index,
//                    fontWeight = FontWeight.ExtraBold
                )
                Row (verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = value,
//                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Tag()
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector =
                            if (isExpanded) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Expand"
                        )
                    }
                }
            }

            if (isExpanded) {
                // Additional content when the row is expanded
                expandedContent()
            }
        }
    }
}

@Composable
fun BodyInfoBox() {
    val homeScreenBlue by remember {
        mutableStateOf( R.color.Home_Screen_Blue)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = homeScreenBlue))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
                .padding(horizontal = 36.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Body Age",
                    fontSize = 16.sp,
                    color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "23.02",
                    fontSize = 25.sp,
                    color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyScoreCircularProgressIndicator(progress = 30f, modifier = Modifier.size(80.dp)) {
                            Column (Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center){
                                Text(text = "Body Score",
                                    color = Color.White,
                                    fontSize = 10.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "30",
                                    color = Color.White,
                                    fontSize = 30.sp)
                            }
                        }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Body Fat",
                    fontSize = 16.sp,
                    color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "14.03%",
                    fontSize = 25.sp,
                    color = Color.White)
            }
        }
        Divider(
            color = Color.White.copy(alpha = 0.5f), // Light color with some transparency
            thickness = (0.9).dp, // Very thin thickness
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp)
        )
        Box (
            Modifier
                .fillMaxWidth()
                .height(120.dp)){
            Text(text = "Compare with 20/07/2024",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.White)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "-43.22 Kg",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Weight",
                            color = Color.White
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "-18.23",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "BMI",
                            color = Color.White
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "-17.67%",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Body Fat(%)",
                            color = Color.White
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun NonExpandableRow(index: String, value: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    isExpanded = !isExpanded
                })
            }) {
        Column (Modifier.fillMaxHeight()){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
                    .padding(8.dp)
                    .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = index,
//                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(start = 80.dp),
//                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthReportScreenPreview() {
    HealthReportScreen( onBackClick = {})
}