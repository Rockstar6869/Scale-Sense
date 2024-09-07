package com.ujjolch.masterapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R

@Composable
fun GenderSelectionScreen(sharedViewModel: SharedViewModel,
                          onContinueClick:()->Unit,
                          userDetailsViewModel: UserDetailsViewModel){
    var selectedGender by remember { mutableStateOf("Male") }
    val userdata by userDetailsViewModel.userData.observeAsState()
    LaunchedEffect(userdata) {
        if(userdata.isNotNull() && userdata!!.gender.isNotBlank()){
            selectedGender = userdata!!.gender
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)) {
            Column (Modifier.padding(top=28.dp)){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = stringResource(id = R.string.genderprompt1),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp)
                }
            }
            Column (
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    selectedGender = "Male"
                                })
                            }
                            .border(
                                width = 2.dp,
                                color = if (selectedGender == "Male") Color.Black else Color.Transparent
                            )) {
                        Image(
                            painter = painterResource(id = R.drawable.male_option2),
                            contentDescription = "Male",
                            modifier = Modifier
                                .height(250.dp)
                                .width(150.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Male")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    selectedGender = "Female"
                                })
                            }
                            .border(
                                width = 2.dp,
                                color = if (selectedGender == "Female") Color.Black else Color.Transparent
                            )) {
                        Image(
                            painter = painterResource(id = R.drawable.femaleimagepotion3),
                            contentDescription = "Female",
                            modifier = Modifier
                                .height(250.dp)
                                .width(150.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Female")
                    }
                }

            }
        Column(Modifier.align(Alignment.BottomCenter)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        sharedViewModel.updateGender(selectedGender)
                        onContinueClick()
                              },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // Customize the background color
                        contentColor = Color.White
                    )
                )
                {
                    Text(
                        text = stringResource(id = R.string.Continue),
                        color = Color.White
                    )
                }
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun GenderSelectionScreenPreview() {
//    GenderSelectionScreen()
//}