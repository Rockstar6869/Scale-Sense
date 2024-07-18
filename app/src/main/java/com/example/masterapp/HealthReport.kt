package com.example.masterapp

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HealthReportScreen(onBackClick: () -> Unit,
                       userDetailsViewModel: UserDetailsViewModel = viewModel()) {

    val userData by userDetailsViewModel.userData.observeAsState()
    val userHistory by userDetailsViewModel.userHist.observeAsState()
    var lastWeight by remember { mutableStateOf(0.0) }
    var lastImpedance by remember { mutableStateOf(0) }
    var secondLastWeight by remember { mutableStateOf(0.0) }
    var secondLastWeightDate by remember { mutableStateOf("") }

    if(!userHistory.isNullOrEmpty()){
        val datehist = userHistory!!.map { it.date }
        val weighthist = userHistory!!.map { it.weight }
        lastWeight = weighthist.last()

        secondLastWeight = weighthist[weighthist.size - 2]
        secondLastWeightDate = datehist[datehist.size - 2]

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

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 40.dp)
                        , horizontalArrangement = Arrangement.Center){
                            Text(text = "Health Report")
                        }
                            },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(top = 12.dp)
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    item{
                        InfoCard(title = "Compare with $secondLastWeightDate",
                            weight = "${(lastWeight-secondLastWeight).format(2).toDouble()} Kg",
                            BMI = "${(BMI-secondLastBMI).format(2).toDouble()}",
                            BodyFatPercent = "${(bodyFatPercent-secondLastBodyFatPercent).format(2).toDouble()}%"
                                , Increased = if(lastWeight-secondLastWeight>0) true else false)
                    }

                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        ExpandableRow(index = "Weight", value = "$lastWeight Kgs") {
                            Row(Modifier.padding(horizontal = 16.dp)) {
                                CustomSlider3(
                                    value = lastWeight.toFloat(),
                                    onValueChange = {},
                                    blueRange = 0f..40f,
                                    greenRange = 40f..80f,
                                    orangeRange = 80f..120f,
                                    blueText = "Underweight",
                                    greenText = "Normal",
                                    orangeText = "Overweight"
                                )
                            }
                        }
                    }
                        item{
                        Spacer(modifier = Modifier.height(4.dp))
                        ExpandableRow(index = "BMI", value = "$BMI") {
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
                                redText = "Overweight"
                            )
                        }}
                        item{
                        Spacer(modifier = Modifier.height(4.dp))
                        ExpandableRow(index = "Body Water%", value = "$bodyWaterPercent%") {
                            BodyWaterPercentSlider(
                                value = bodyWaterPercent.toFloat(),
                                onValueChange = {},
                                blueRange = 0f..50f,
                                greenRange = 50f..65f,
                                orangeRange = 65f..115f,
                                blueText = "Low",
                                greenText = "Standard",
                                orangeText = "High"
                            )
                        }
                    }
                        item{
                        Spacer(modifier = Modifier.height(4.dp))
                        ExpandableRow(index = "Body Fat%", value = "$bodyFatPercent%") {
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
                                redText = "Over"
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    item{
                        ExpandableRow(index = "Lean Body Mass%", value = "$LeanBodyMassPercent%") {
                            LeanBodyMassPercentSlider(
                                value = LeanBodyMassPercent.toFloat(),
                                onValueChange = {},
                                blueRange = 0f..68f,
                                greenRange = 68f..90f,
                                orangeRange = 90f..100f,
                                blueText = "Low",
                                greenText = "Standard",
                                orangeText ="High"
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    item{
                        ExpandableRow(index = "BMR", value = "$BMR kcal") {
                            BMRSlider(
                                value = BMR.toFloat(),
                                onValueChange = {},
                                blueRange = 0f..1450f,
                                greenRange = 1450f..1650f,
                                darkGreenRange = 1650f..3000f,
                                blueText = "Low",
                                greenText = "Good",
                                darkGreenText ="Excellent"
                            )
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                    }

                })

    }

@Composable
fun ExpandableRow(index: String, value: String, expandedContent: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray, // Use a light color for the border
                shape = RectangleShape
            )
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = index,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(start = 80.dp),
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector =
                        if (isExpanded) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Expand"
                    )
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
fun InfoCard(title: String,weight:String,BMI:String,BodyFatPercent:String,
                Increased:Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row (Modifier.fillMaxWidth()){
                        if (Increased) Icon(painter = painterResource(id = R.drawable.baseline_increase), contentDescription = "increased", tint = Color.Green
                            ,modifier = Modifier.size(30.dp))
                        else Icon(painter = painterResource(id = R.drawable.baseline_decrease), contentDescription = "decreased",tint = Color.Red
                            ,modifier = Modifier.size(30.dp))
                        Text(text = weight, fontSize = 20.sp)
                    }
                    Text(text = "Weight", fontSize = 14.sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row (Modifier.fillMaxWidth()){
                        if (Increased) Icon(painter = painterResource(id = R.drawable.baseline_increase), contentDescription = "increased", tint = Color.Green
                            ,modifier = Modifier.size(30.dp))
                        else Icon(painter = painterResource(id = R.drawable.baseline_decrease), contentDescription = "decreased",tint = Color.Red
                            ,modifier = Modifier.size(30.dp))
                        Text(text = BMI, fontSize = 20.sp)
                    }
                    Text(text = "BMI", fontSize = 14.sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row (Modifier.fillMaxWidth()){
                        if (Increased) Icon(painter = painterResource(id = R.drawable.baseline_increase), contentDescription = "increased", tint = Color.Green
                            ,modifier = Modifier.size(30.dp))
                        else Icon(painter = painterResource(id = R.drawable.baseline_decrease), contentDescription = "decreased",tint = Color.Red
                            ,modifier = Modifier.size(30.dp))
                        Text(text = BodyFatPercent, fontSize = 20.sp)
                    }
                    Text(text = "Body Fat%", fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthReportScreenPreview() {
    HealthReportScreen( onBackClick = {})
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    InfoCard(title = "Health Stats","40 Kg","18.7","40%",true)
}