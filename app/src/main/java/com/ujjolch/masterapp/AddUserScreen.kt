package com.ujjolch.masterapp

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R

@Composable
fun AddUserScreen(userDetailsViewModel: UserDetailsViewModel,
                  onNavigateBack:()->Unit) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female")
    var expanded by remember { mutableStateOf(false) }

    val userUnits by userDetailsViewModel.units.observeAsState()
    var heightUnit by remember { mutableStateOf("") }
    var feet by remember { mutableStateOf("") }
    var inch by remember { mutableStateOf("") }
    var convertedHeightToCm by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    LaunchedEffect(userUnits) {
        if(userUnits.isNotNull()){
            heightUnit = userUnits!!.heightunit
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
                        androidx.compose.material.Text(text = stringResource(id = R.string.AddProfile),
                            color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack()}) {
                        Icon(
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
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            })
                        }
                ) {
                    Column(
                        Modifier
                            .fillMaxHeight(0.55f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Bottom) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                modifier = Modifier.size(100.dp),
                                tint = colorResource(id = R.color.Icon_Tint)
                            )
                        }
                        UserDetailTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = "First Name",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                        )
                        Divider(
                            color = Color.LightGray, thickness = 0.9.dp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        UserDetailTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = "Last Name",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                        )
                        Divider(
                            color = Color.LightGray, thickness = 0.9.dp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(Modifier.height(18.dp))
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
                                    androidx.compose.material.Text(
                                        text = "Gender", style = TextStyle(
                                            color = Color.Gray,
                                            fontSize = 16.sp
                                        )
                                    )
                                    androidx.compose.material.Text(text = gender)
                                }
                            }

                        }
                        Spacer(Modifier.height(2.dp))
                        Divider(
                            color = Color.LightGray, thickness = 0.9.dp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        UserDetailTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = "Age",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Divider(
                            color = Color.LightGray, thickness = 0.9.dp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        if (heightUnit == "" || heightUnit == "cm") {
                        UserDetailTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = "Height(CM)",
                            modifier = Modifier.fillMaxWidth()
                        )
                            Divider(
                                color = Color.LightGray, thickness = 0.9.dp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
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
                                        label = "Height(Ft)"
                                    )
                                    Divider(
                                        color = Color.LightGray, thickness = 0.9.dp,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp)
                                ) {
                                    UserDetailTextField(
                                        value = inch,
                                        onValueChange = { inch = it },
                                        label = "(In)"
                                    )
                                    Divider(
                                        color = Color.LightGray, thickness = 0.9.dp,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }
//                        Divider(color = Color.LightGray, thickness = 0.9.dp,
//                            modifier = Modifier.padding(horizontal = 4.dp))
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
                                    androidx.compose.material.Text(text = option)
                                }
                            }
                        }
                    }
                    Column (
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            onClick = {
                                if(height.isNotBlank() && (heightUnit == "" || heightUnit== "cm") && isDouble(height)){
                                    convertedHeightToCm = height
                                }
                                else if((feet.isNotBlank() && inch.isNotBlank()) && heightUnit == "in" && (isInteger(feet) && isDouble(inch))){
                                    convertedHeightToCm = (Calculate.convertFeetAndInchesToCm(feet.toInt(),inch.toDouble())).toString()
                                }

                                if (firstName.isNotBlank() && lastName.isNotBlank() && gender.isNotBlank() && age.isNotBlank() && convertedHeightToCm.isNotBlank() && isInteger(age)
                                ) {
                                    if (convertedHeightToCm.toDouble() in 100.0..250.0)
                                    {
                                        userDetailsViewModel.addSubUser(
                                            subuser = subuser(
                                                firstName = firstName,
                                                lastName = lastName
                                            ), ud = UserData(
                                                convertedHeightToCm.toDouble(),
                                                gender,
                                                ageToMillis((age.toDouble()).toInt())
                                            )
                                        )

                                    onNavigateBack()
                                }
                                    else{
                                        Toast.makeText(context,
                                            "Please enter a valid height",
                                            Toast.LENGTH_SHORT).show()
                                    }
                            }
                            },
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Customize the background color
                                contentColor = Color.White)
                        ) {
                            Text(stringResource(id = R.string.SaveData))
                        }
                    }
                }
            }
        }
    )

}