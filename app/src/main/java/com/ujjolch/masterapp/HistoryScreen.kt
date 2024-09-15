package com.ujjolch.masterapp


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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HistoryScreen(userDetailsViewModel: UserDetailsViewModel = viewModel()) {
    val homeScreenBlue by remember {
        mutableStateOf(R.color.Home_Screen_Blue)
    }
    val homeScreenWhite by remember {
        mutableStateOf(R.color.Home_Screen_White)
    }
    val tabUnSelectedWhite by remember {
        mutableStateOf(R.color.Tab_UnSelected)
    }
    val selectedTabIndex = remember { mutableStateOf(0) }
    var selectedTimeRange by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val userdata by userDetailsViewModel.userData.observeAsState()
    val age by userDetailsViewModel.age.observeAsState()
    val userHist by userDetailsViewModel.userHist.observeAsState()
    val weighthist = userHist?.map { it.weight } ?: listOf(1.2, 1.2)
    val impedancehist = userHist?.map { it.impedance } ?: listOf(1)
    val userUnits by userDetailsViewModel.units.observeAsState()

    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            userDetailsViewModel.gethistlist()
        }
    }

    Scaffold(
        topBar = {
            Column{
            TopAppBar(
                title = {
                    Row(
                        Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.History),
                            color = Color.Black
                        )
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp
            )
                TimeRow(onSelect = {
                    if(it == "Day"){
                        selectedTimeRange = 0
                    }
                    else if(it == "Month"){
                        selectedTimeRange = 1
                    }
                    else if(it == "Year"){
                        selectedTimeRange = 2
                    }

                }
                )
        }
        },
        content = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier.padding(top = 60.dp))
//            Text(
//                text = stringResource(id = R.string.History),
//                    fontWeight = FontWeight.Bold,
//                fontSize = 40.sp,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//            HorizontalDivider(thickness = 1.dp, color = Color.Black, modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp))
//            Spacer(modifier = Modifier.padding(top = 80.dp))

            if (!userHist.isNotNull()) {

            }
//                else if (userHist.isNotNull() && (userHist?.size)!!< 2) {
//                    Spacer(modifier = Modifier.padding(top = 120.dp))
//                    Text(
//                        text = "You Need To Measure your Weight For at Least 2 days to See your History Graph",
//                        modifier = Modifier.padding(8.dp),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//                }
            else {
//                if(userHist.isNotNull()) {
//
//                    Text("${(getMonthsAndYears(userHist!!))}")
//                }
                // Content based on selected tab
                when (selectedTabIndex.value) {
                    0 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            WeightGraph(userHist = userHist!!, userUnits?.weightunit ?: "kg",selectedTimeRange)
                        }
                    }

                    1 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            BMIGraph(
                                userHist = userHist!!,
                                HeightInCM = userdata?.heightincm ?: 0.0,
                                selectedTimeRange
                            )
                        }
                    }

                    2 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            BodyWaterPercentGraph(
                                userHist = userHist!!,
                                HeightInCM = userdata?.heightincm ?: 0.0,
                                Age = age ?: 0,
                                Gender = if (userdata?.gender == "Male") "M" else "F",
                                selectedTimeRange
                            )
                        }
                    }

                    3 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            BodyFatPercentGraph(
                                userHist = userHist!!,
                                HeightInCM = userdata?.heightincm ?: 0.0,
                                Age = age ?: 0,
                                Gender = if (userdata?.gender == "Male") "M" else "F",
                                selectedTimeRange
                            )
                        }
                    }

                    4 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            SkeletalMuscleGraph(
                                userHist = userHist!!,
                                HeightInCM = userdata?.heightincm ?: 0.0, Age = age ?: 0,
                                Gender = if (userdata?.gender == "Male") "M" else "F",
                                selectedTimeRange
                            )
                        }
                    }

                    5 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            LeanBodyMassGraph(
                                userHist = userHist!!,
                                Age = age ?: 0,
                                HeightInCM = userdata?.heightincm ?: 0.0,
                                Gender = if (userdata?.gender == "Male") "M" else "F",
                                selectedTimeRange
                            )
                        }
                    }

                    6 -> if (!userHist.isNullOrEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 190.dp)
                        ) {
                            BMRGraph(
                                userHist = userHist!!,
                                Age = age ?: 0,
                                HeightInCM = userdata?.heightincm ?: 0.0,
                                Gender = if (userdata?.gender == "Male") "M" else "F",
                                selectedTimeRange
                            )
                        }
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
            backgroundColor = colorResource(id = homeScreenWhite)
        ) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val screenWidth = configuration.screenWidthDp.dp
            val dynamicHeight = (screenHeight.value / 8.77).dp

            listOfTabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex.value == index

                Row {
                    Tab(
                        modifier = Modifier
                            .height(dynamicHeight)
                            .width(if(screenWidth.value<600)120.dp else 200.dp)
                            .background(
                                if (isSelected) Color.White else colorResource(id = tabUnSelectedWhite)
                            ),
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            scope.launch {
                                selectedTabIndex.value = index
                            }
                        },
                        text = null,
                        icon = {
                            Column(verticalArrangement = Arrangement.Top) {
                                if (isSelected) {
                                    HorizontalDivider(
                                        color = colorResource(id = homeScreenBlue),
                                        thickness = 4.dp
                                    )

                                }
                            }
                            val tint = if (isSelected) colorResource(id = homeScreenBlue)
                            else Color.Black
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = tab.icon),
                                    contentDescription = "Icon",
                                    tint = tint,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = tab.title),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
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
)
}