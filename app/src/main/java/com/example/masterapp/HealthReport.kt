package com.example.masterapp

import android.nfc.Tag
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
    var isComparableHistAvailable by remember {
        mutableStateOf(false)
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
            isComparableHistAvailable = true
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
    var VFI by remember { mutableStateOf(0.0) }
    var AMR by remember { mutableStateOf(0.0) }
    var BoneWeightPercent by remember { mutableStateOf(0.0) }
    var SubcutaneousFat by remember { mutableStateOf(0.0) }
    var InorganicSalt by remember { mutableStateOf(0.0) }
    var ProtienPercent by remember { mutableStateOf(0.0) }
    var MusclePercent by remember { mutableStateOf(0.0) }
    var selectedOptionForAMR by remember {
        mutableStateOf("e")
    }
    var secondLastBodyFatPercent by remember { mutableStateOf(0.0) }
    var secondLastBMI by remember { mutableStateOf(0.0) }
    BMI = Calculate.BMI(userData?.heightincm?:0.0,lastWeight)
    InorganicSalt = Calculate.InorganicSalt(lastWeight)
    BoneWeightPercent = Calculate.BoneWeightPercent(lastWeight,userData?.heightincm?:0.0,userData?.age?:0)
    VFI = Calculate.VisceralFatIndex(userData!!.heightincm,lastWeight,userData!!.age)
    ProtienPercent = Calculate.ProtienPercentage(lastWeight, bodyFatPercent)
    MusclePercent = Calculate.MusclePercent(bodyWaterPercent,lastWeight,bodyFatPercent)
    if(userData?.gender == "Male"){
        bodyWater = Calculate.BodyWaterForMale(userData!!.age,userData!!.heightincm,lastWeight)
        bodyFatPercent = Calculate.BodyFatPercentforMale(userData!!.age,BMI)
        LeanBodyMass = Calculate.LeanBodyMass(lastWeight,bodyFatPercent)
        LeanBodyMassPercent = Calculate.LeanBodyMassPercent(LeanBodyMass,lastWeight)
        BMR = Calculate.BMRforMale(lastWeight,userData!!.heightincm,userData!!.age)
        SubcutaneousFat = Calculate.SubcutaneousFatForMale(lastWeight,userData!!.heightincm,userData!!.age)

        LaunchedEffect(selectedOptionForAMR) {
            AMR = Calculate.AMRforMale(lastWeight,userData!!.heightincm,userData!!.age,selectedOptionForAMR)
        }


        secondLastBodyFatPercent = Calculate.BodyFatPercentforMale(userData!!.age,secondLastBMI)
    }
    else if(userData?.gender == "Female"){
        bodyWater = Calculate.BodyWaterForFemale(userData!!.age,userData!!.heightincm,lastWeight)
        bodyFatPercent = Calculate.BodyFatPercentforFemale(userData!!.age,BMI)
        LeanBodyMass = Calculate.LeanBodyMass(lastWeight,bodyFatPercent)
        LeanBodyMassPercent = Calculate.LeanBodyMassPercent(LeanBodyMass,lastWeight)
        BMR = Calculate.BMRforFemale(lastWeight,userData!!.heightincm,userData!!.age)
        SubcutaneousFat = Calculate.SubcutaneousFatForFemale(lastWeight,userData!!.heightincm,userData!!.age)

        LaunchedEffect(selectedOptionForAMR) {
            AMR = Calculate.AMRforFemale(lastWeight,userData!!.heightincm,userData!!.age,selectedOptionForAMR)
        }


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
                        BodyInfoBox(
                            bodyAge = (userData!!.age).toString(),
                            bodyScore = "0",
                            bodyFat = bodyFatPercent.toString(),
                            prevDate = secondLastWeightDate,
                            wdiff = "${(lastWeight-secondLastWeight).format(2).toDouble()}",
                            BMIdiff = "${(BMI-secondLastBMI).format(2).toDouble()}",
                            BFTdiff = "${(bodyFatPercent-secondLastBodyFatPercent).format(2).toDouble()}",
                            isComparableHistAvailable = isComparableHistAvailable
                        )
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
                                    blueRange = 35f..50f,
                                    greenRange = 50f..65f,
                                    orangeRange = 65f..80f,
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
                                    value = if(LeanBodyMassPercent<100f) LeanBodyMassPercent.toFloat() else 112f,
                                    onValueChange = {},
                                    blueRange = 46f..68f,
                                    greenRange = 68f..90f,
                                    orangeRange = 90f..112f,
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

                                    }
                                }) {
                                BMRSlider(
                                    value = BMR.toFloat(),
                                    onValueChange = {},
                                    blueRange = 1250f..1450f,
                                    greenRange = 1450f..1650f,
                                    darkGreenRange = 1650f..1850f,
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
                                ExpandableRowForAMR(index = "AMR",
                                    value = "$AMR Kcal",
                                    Tag = { },
                                    onSelectedOptionChange = {
                                        if(it == "(Sedentary)"){
                                            selectedOptionForAMR = "e"
                                        }
                                        else if(it == "(Lightly active)"){
                                            selectedOptionForAMR = "la"
                                        }
                                        else if(it == "(Moderately active)"){
                                            selectedOptionForAMR = "ma"
                                        }
                                        else if(it == "(Active)"){
                                            selectedOptionForAMR = "a"
                                        }
                                        else if(it == "(Extremely active)"){
                                            selectedOptionForAMR = "ea"
                                        }
                                    },
                                    expandedContent = {
                                        //Slider
                                    })
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f), // Light color with some transparency
                                    thickness = 1.dp, // Very thin thickness
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                ExpandableRow(index = "Bone%",
                                    value = "$BoneWeightPercent%",
                                    Tag = {
                                        if(BoneWeightPercent<=10){
                                            BlueTag(text = "Low")
                                        }
                                        else if(BoneWeightPercent<=15){
                                            GreenTag(text = "Standard")
                                        }
                                        else if(BoneWeightPercent>15){
                                            DarkGreenTag(text = "Excellent")
                                        }
                                    }) {
                                    BonePercentSlider(
                                        value = BoneWeightPercent.toFloat(),
                                        onValueChange = {},
                                        blueRange = 5f..10f,
                                        greenRange = 10f..15f,
                                        darkGreenRange = 15f..20f,
                                        blueText = "Low",
                                        greenText = "Standard",
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
                                ExpandableRow(index = "Protein%",
                                    value = "$ProtienPercent%",
                                    Tag = {
                                        if(ProtienPercent<=15.9){
                                            BlueTag(text = "Low")
                                        }
                                        else if(ProtienPercent>=16 && ProtienPercent<=20.1){
                                            GreenTag(text = "Standard")
                                        }
                                        else{
                                            OrangeTag(text = "High")
                                        }
                                    }) {
                                    ProteinPercentSlider(
                                        value = ProtienPercent.toFloat(),
                                        onValueChange = {},
                                        blueRange = 11.9f..16f,
                                        greenRange = 16f..20.1f,
                                        orangeRange = 20.1f..24.2f,
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
                                ExpandableRow(index = "Muscle%",
                                    value = "$MusclePercent%",
                                    Tag = {
                                        if(MusclePercent<73){
                                            BlueTag(text = "Low")
                                        }
                                        else if(MusclePercent<81){
                                            GreenTag(text = "Standard")
                                        }
                                        else if(MusclePercent>=81){
                                            DarkGreenTag(text = "Excellent")
                                        }
                                    }) {
                                    MusclePercentSlider(
                                        value = MusclePercent.toFloat(),
                                        onValueChange = {},
                                        blueRange = 65f..73f,
                                        greenRange = 73f..81f,
                                        darkGreenRange = 81f..89f,
                                        blueText = "Low",
                                        greenText = "Standard",
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
                                ExpandableRow(index = "Inorganic Salt",
                                    value = "$InorganicSalt",
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
                                    value = "$VFI",
                                    Tag = {
                                        if(VFI<10){
                                            GreenTag(text = "Standard")

                                        }                 
                                        else if(VFI<=17){
                                            OrangeTag(text = "High")
                                        }
                                        else if(VFI>17){
                                            RedTag(text = "Over")
                                        }
                                    }) {
                                    VisceralFatIndexSlider(
                                        value = VFI.toFloat(),
                                        onValueChange = {},
                                        greenRange = 0f..10f,
                                        orangeRange = 10f..17f,
                                        redRange = 17f..25f,
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
                                ExpandableRow(index = "Subcutaneous Fat",
                                    value = "$SubcutaneousFat Kg",
                                    Tag = {
                                        if(SubcutaneousFat<5){
                                            BlueTag(text = "Luw")
                                        }
                                        else if(SubcutaneousFat<=10.5){
                                            GreenTag(text = "Standard")
                                        }
                                        else{
                                            OrangeTag(text = "High")
                                        }

                                    }) {
                                    SubcutaneousFatSlider(
                                        value = SubcutaneousFat.toFloat(),
                                        onValueChange = {},
                                        blueRange = 0f..5f,
                                        greenRange = 5f..10.5f,
                                        orangeRange = 10.5f..15f,
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
                                ExpandableRow(index = "Body Age",
                                    value = "${userData?.age?:0}",
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
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                val dynamicFontSize = if(screenWidth<600.dp)(screenWidth.value/26.2).sp else 15.sp
                Text(
                    text = index,
                    fontSize = dynamicFontSize
//                    fontWeight = FontWeight.ExtraBold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = value,
                        fontSize = dynamicFontSize
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
fun BodyInfoBox(
    bodyAge:String,
    bodyScore:String,
    bodyFat:String,
    prevDate:String,
    wdiff:String,
    BMIdiff:String,
    BFTdiff:String,
    isComparableHistAvailable:Boolean
) {
    val homeScreenBlue by remember {
        mutableStateOf( R.color.Home_Screen_Blue)
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val distancing = if(screenWidth<600.dp) 32.dp else 52.dp //distance after compare with
    val distancing2 = if(screenWidth<600.dp) 0.dp else 30.dp
    val lowerboxheight = if(screenWidth<600.dp) 120.dp else 170.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = homeScreenBlue))
    ) {
        Spacer(modifier = Modifier.height(distancing2))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
                .padding(horizontal = 36.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Body Age",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bodyAge,
                    fontSize = 25.sp,
                    color = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyScoreCircularProgressIndicator(
                    progress = bodyScore.toFloat(),
                    modifier = Modifier.size(80.dp)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Body Score",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = bodyScore,
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Body Fat",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$bodyFat%",
                    fontSize = 25.sp,
                    color = Color.White
                )
            }
        }
        if(isComparableHistAvailable){
            Spacer(modifier = Modifier.height(distancing2))
        Divider(
            color = Color.White.copy(alpha = 0.5f), // Light color with some transparency
            thickness = (0.9).dp, // Very thin thickness
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(lowerboxheight)
        ) {
            Text(
                text = "Compare with $prevDate",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.White
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(distancing))

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
                            text = "${SignAdder(wdiff)} Kg",
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
                            text = SignAdder(BMIdiff),
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
                            text = "${SignAdder(BFTdiff)}%",
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
@Composable
fun ExpandableRowForAMR(index: String, value: String,
                  Tag:@Composable () -> Unit,
                  expandedContent: @Composable () -> Unit,
                        onSelectedOptionChange:(selectedOption:String)->Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("(Sedentary)") }
    var selectedOnce by remember { mutableStateOf(false) }

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
                Text(text = if(!selectedOnce)"Select your activity level" else selectedOption,
                    modifier = Modifier
                        .clickable { isMenuExpanded = !isMenuExpanded }
                        .padding(start = 8.dp),
                        fontSize = 12.sp)
                Icon(imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "drop down",
                    modifier = Modifier.size(15.dp))
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        selectedOption = "(Sedentary)"
                        onSelectedOptionChange(selectedOption)
                        selectedOnce = true
                        isMenuExpanded = false
                    },
                        modifier = Modifier.width(300.dp)) {
                        Text("Sedentary (little or no exercise)",
                            color = if(selectedOption == "(Sedentary)") colorResource(id = R.color.Home_Screen_Blue)
                                    else Color.Black)
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "(Lightly active)"
                        onSelectedOptionChange(selectedOption)
                        selectedOnce = true
                        isMenuExpanded = false
                    },
                        modifier = Modifier.width(300.dp)) {
                        Text("Lightly active (1–3 days/week)",
                            color = if(selectedOption == "(Lightly active)") colorResource(id = R.color.Home_Screen_Blue)
                            else Color.Black)
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "(Moderately active)"
                        onSelectedOptionChange(selectedOption)
                        selectedOnce = true
                        isMenuExpanded = false
                    },
                        modifier = Modifier.width(300.dp)) {
                        Text("Moderately active (3–5 days/week)",
                            color = if(selectedOption == "(Moderately active)") colorResource(id = R.color.Home_Screen_Blue)
                            else Color.Black)
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "(Active)"
                        onSelectedOptionChange(selectedOption)
                        selectedOnce = true
                        isMenuExpanded = false
                    },
                        modifier = Modifier.width(300.dp)) {
                        Text("Active (5–6 days/week)",
                            color = if(selectedOption == "(Active)") colorResource(id = R.color.Home_Screen_Blue)
                            else Color.Black)
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "(Extremely active)"
                        onSelectedOptionChange(selectedOption)
                        selectedOnce = true
                        isMenuExpanded = false
                    },
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text("Extremely active (6–7 days/week)",
                            color = if(selectedOption == "(Extremely active)") colorResource(id = R.color.Home_Screen_Blue)
                            else Color.Black)
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun HealthReportScreenPreview() {
    HealthReportScreen( onBackClick = {})
}