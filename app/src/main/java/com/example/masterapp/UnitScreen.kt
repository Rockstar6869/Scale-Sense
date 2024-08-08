package com.example.masterapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.common.extensions.isNotNull

@Composable
fun UnitScreen(onNavigateToMeScreen:()->Unit,
               userDetailsViewModel: UserDetailsViewModel){

    val currentUnits by userDetailsViewModel.units.observeAsState()

    var selectedWeight by remember {
        mutableStateOf("")
    }
    var selectedHeight by remember {
        mutableStateOf("")
    }
    LaunchedEffect(currentUnits) {
        if(currentUnits.isNotNull()){
            selectedHeight = currentUnits!!.heightunit
            selectedWeight = currentUnits!!.weightunit
        }
        else{
            selectedHeight = "cm"
            selectedWeight = "kg"
        }
    }

    var confirm by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(confirm){
        if(confirm) {
            userDetailsViewModel.uploadUnit(
                Units(
                    weightunit = selectedWeight,
                    heightunit = selectedHeight
                )
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp)
                        , horizontalArrangement = Arrangement.Center){
                        androidx.compose.material.Text(text = "Unit",
                            color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToMeScreen()}) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp)
        },
        content = {
    Box (
        Modifier
            .fillMaxSize()
            .padding(it)){
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 120.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column{
            Text(
                text = "Weight",
                color = colorResource(id = R.color.Home_Screen_Blue)
            )
            Spacer(modifier = Modifier.height(20.dp))
            listOfWeightUnits.forEach {
                UnitRow(index = it, isSelected = selectedWeight == it) {
                    selectedWeight = it
                }
                Spacer(modifier = Modifier.padding(top = 12.dp))
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Height",
                color = colorResource(id = R.color.Home_Screen_Blue)
            )
            Spacer(modifier = Modifier.height(20.dp))
            listOfHeightUnits.forEach {
                UnitRow(index = it, isSelected = selectedHeight == it) {
                    selectedHeight = it
                }
                Spacer(modifier = Modifier.padding(top = 12.dp))
            }
        }
            Button(onClick = {
                if(selectedHeight.isNotBlank() && selectedWeight.isNotBlank()){
                confirm = true
                onNavigateToMeScreen()
                }
            },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White)) {

                Text(text = "Confirm")
            }
            }
        }
})
    }

@Composable
fun UnitRow(index:String,isSelected: Boolean, Select:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.Home_Screen_White),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    Select()
                })
            }
    ) {
        Text(
            text = index,
            modifier = Modifier.weight(1f),
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_check_circle),
            contentDescription = "Check Icon",
            tint = if (isSelected) Color.Green else Color.Gray
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewUnitScreen() {
//    UnitScreen({})
//}