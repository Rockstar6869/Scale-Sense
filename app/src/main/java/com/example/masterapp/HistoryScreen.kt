package com.example.masterapp


import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@Composable
fun HistoryScreen(userDetailsViewModel: UserDetailsViewModel = viewModel()){
    val selectedTabIndex = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val userdata by userDetailsViewModel.userData.observeAsState()
    val userHist  by userDetailsViewModel.userHist.observeAsState()
    val weighthist = userHist?.map { it.weight } ?: listOf(1.2,1.2)
    val impedancehist = userHist?.map { it.impedance } ?: listOf(1)

//    LaunchedEffect(true) {
//            val timer = Timer()
//            timer.schedule(object : TimerTask() {
//                override fun run() {
//                    userHist?.forEach{
//                        Log.d("UJC","${it.date}")
//                    }
//                }
//            }, 0, 1000)
//
//        }

    Box(modifier = Modifier
        .fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(top = 60.dp))
            Text(
                text = "History",
                    fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black, modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp))
            Spacer(modifier = Modifier.padding(top = 80.dp))

            if((weighthist.size)<2){
                Spacer(modifier =  Modifier.padding(top = 120.dp))
                Text(text = "You Need At Least 2 Measurements to See your History Graph",
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            else {

                // Content based on selected tab
                    when (selectedTabIndex.value) {
                        0 -> if(!userHist.isNullOrEmpty()){
                            WeightGraph(userHist = userHist!!)}
                        1 -> if(!userHist.isNullOrEmpty()){BMIGraph(
                            userHist = userHist!!,
                            HeightInCM = userdata?.heightincm ?: 0.0
                        )}

                        2 -> if(!userHist.isNullOrEmpty()){BodyWaterPercentGraph(
                            userHist = userHist!!,
                            HeightInCM = userdata?.heightincm ?: 0.0,
                            Age = userdata?.age ?: 0,
                            Gender = if (userdata?.gender == "Male") "M" else "F"
                        )}

                        3 -> if(!userHist.isNullOrEmpty()){BodyFatPercentGraph(
                            userHist = userHist!!,
                            HeightInCM = userdata?.heightincm ?: 0.0,
                            Age = userdata?.age ?: 0,
                            Gender = if (userdata?.gender == "Male") "M" else "F"
                        )}

                        4 -> if(!userHist.isNullOrEmpty()){SkeletalMuscleGraph(
                            userHist = userHist!!,
                            HeightInCM = userdata?.heightincm ?: 0.0, Age = userdata?.age ?: 0,
                            Gender = if (userdata?.gender == "Male") "M" else "F"
                        )}

                        5 -> if(!userHist.isNullOrEmpty()){
                            LeanBodyMassGraph(
                            userHist = userHist!!,
                            Age = userdata?.age ?: 0,
                            HeightInCM = userdata?.heightincm ?: 0.0,
                            Gender = if (userdata?.gender == "Male") "M" else "F"
                        )}

                        6 -> if(!userHist.isNullOrEmpty()){
                            BMRGraph(
                            userHist = userHist!!,
                            Age = userdata?.age ?: 0,
                            HeightInCM = userdata?.heightincm ?: 0.0,
                            Gender = if (userdata?.gender == "Male") "M" else "F"
                        )
                        }
                    }
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex.value,
            edgePadding = TabRowDefaults.DividerThickness,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 80.dp),
            backgroundColor = colorResource(id = R.color.Home_Screen_White)
        ) {
            listOfTabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex.value == index
                Row {
                    Tab(
                        modifier = Modifier
                            .height(120.dp)
                            .width(120.dp)
                            .background(
                                if (isSelected) Color.White else colorResource(id = R.color.Home_Screen_White)
                            ),
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            scope.launch {
                                selectedTabIndex.value = index
                            }
                        },
                        text = { Text(text = tab.title) },
                        icon = {
                            val tint = if (isSelected) Color.Blue else Color.Black
                            Icon(
                                tint = tint,
                                painter = painterResource(id = tab.icon),
                                contentDescription = "Icon"
                            )
                        }
                    )
                    if (index < listOfTabs.size - 1) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .height(56.dp)  // Adjust height to match the tab height
                                .width(1.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}