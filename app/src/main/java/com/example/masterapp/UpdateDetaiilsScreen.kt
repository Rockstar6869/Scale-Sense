package com.example.masterapp

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import java.text.SimpleDateFormat

@Composable
fun UpdateDetailScreen(userDetailsViewModel: UserDetailsViewModel = viewModel(),
                       onNavigateToAddDevice:()->Unit,
                       showUnitChanger:Boolean = false){
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var feet by remember { mutableStateOf("") }
    var inch by remember { mutableStateOf("") }

    var convertedHeightToCm by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female")
    var expanded by remember { mutableStateOf(false) }

    val currentUserDetails by userDetailsViewModel.userData.observeAsState()
    val userUnits by userDetailsViewModel.units.observeAsState()
    var heightUnit by remember { mutableStateOf("") }
    var heightUnitExpanded by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(currentUserDetails) {
        if(currentUserDetails.isNotNull()){
            gender = currentUserDetails!!.gender
            age = currentUserDetails!!.age.toString()
            height = currentUserDetails!!.heightincm.toString()
        }
    }
    LaunchedEffect(userUnits) {
        if(userUnits.isNotNull()){
            heightUnit = userUnits!!.heightunit
        }
    }
    LaunchedEffect(currentUserDetails,userUnits,heightUnit) {
        if(userUnits.isNotNull() && currentUserDetails.isNotNull()){
            if(heightUnit == "in"){
                val(convertedFeet, convertedInch) = Calculate.convertCmToFeetAndInches(height.toDouble())
                feet = convertedFeet.toString()
                inch = convertedInch.format(2)
            }
        }
    }


    Box (
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    expanded = true
                                })
                            }
                            .padding(horizontal = 16.dp)) {
                        Column {
                            Text(
                                text = "Gender", style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            )
                            Text(text = gender)
                        }
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))
                Divider(color = Color.LightGray, thickness = 0.9.dp)
                Spacer(modifier = Modifier.height(8.dp))
                UserDetailTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = age,
                    onValueChange = { age = it },
                    label = "Age",
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Divider(color = Color.LightGray, thickness = 0.9.dp)
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    if (heightUnit == "" || heightUnit == "cm") {
                        UserDetailTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = height,
                            onValueChange = { height = it },
                            label = "Height(CM)",
                            visualTransformation = VisualTransformation.None,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(color = Color.LightGray, thickness = 0.9.dp)
                    }
                    else if (heightUnit == "in") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween // Spacing between text fields
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                            ) {
                                UserDetailTextField(
                                    value = feet,
                                    onValueChange = { feet = it },
                                    label = "Feet"
                                )
                                HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                UserDetailTextField(
                                    value = inch,
                                    onValueChange = { inch = it },
                                    label = "In"
                                )
                                HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    if(showUnitChanger){
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
//
                            UnitSelector(onSelect = {heightUnit = it} )
                    }
                }
            }
                Spacer(modifier = Modifier.height(8.dp))

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            focusManager.clearFocus()
                            gender = option
                            expanded = false
                        }) {
                            Text(text = option)
                        }
                    }
                }
            }
            Button(
                onClick = {
                    if(height.isNotBlank() && (heightUnit == "" || heightUnit== "cm") && isDouble(height)){
                        convertedHeightToCm = height
                    }
                    else if((feet.isNotBlank() && inch.isNotBlank()) && heightUnit == "in" && (isInteger(feet) && isDouble(inch))){
                        convertedHeightToCm = (Calculate.convertFeetAndInchesToCm(feet.toInt(),inch.toDouble())).toString()
                    }
                    if(age.isNotBlank() && gender.isNotBlank() && convertedHeightToCm.isNotBlank() && isInteger(age)) {
                        userDetailsViewModel.uploadDetails(
                            ud = UserData(
                                convertedHeightToCm.toDouble(),
                                gender = gender,
                                age = age.toInt()
                            )
                        )
                        onNavigateToAddDevice()
                    }

                          },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White
                )
            ) {
                Text(text = "Save Data", fontSize = 18.sp, color = Color.White)
            }
        }
    }

}
@Composable
fun UnitSelector(onSelect:(unitSelected:String)->Unit) {
    var selectedUnit by remember { mutableStateOf("cm") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .height(30.dp)
            .width(100.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
    ) {
        // First part - "cm"
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            selectedUnit = "cm"
                            onSelect("cm")
                        }
                    )
                }
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "cm",
                color = if (selectedUnit == "cm") colorResource(id = R.color.Home_Screen_Blue) else Color.Black,
                textAlign = TextAlign.Center
            )
        }

        // Divider
        VerticalDivider(
            color = Color.Gray,
            modifier = Modifier
                .width(1.dp)
                .padding(4.dp)
        )

        // Second part - "in"
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            selectedUnit = "in"
                            onSelect("in")
                        }
                    )
                }
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "in",
                color = if (selectedUnit == "in") colorResource(id = R.color.Home_Screen_Blue) else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UserDetailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions= KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = visualTransformation,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        )
    )
}

@Preview(showBackground = true)
@Composable
fun UpdateDetailScreenPreview() {
    UpdateDetailScreen(showUnitChanger = false,
        onNavigateToAddDevice = {})
}