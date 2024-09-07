package com.ujjolch.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R

//@Composable
//fun AgeSelectionScreen(){
//    var pickerValue by remember { mutableStateOf(0) }
//    Box (Modifier.fillMaxSize()
//        .padding(bottom = 80.dp)){
//            Column(Modifier.padding(top=8.dp)) {
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = stringResource(id = R.string.yourage),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 25.sp)
//            }
//        }
//            Column (Modifier.align(Alignment.Center),
//                verticalArrangement = Arrangement.Center){
//                    AgePicker(initialValue = 27) {
//                        pickerValue = it
//                    }
//            }
//        Button(
//            onClick = { /* Handle continue action */ },
//            modifier = Modifier
//                .width(200.dp)
//                .height(50.dp)
//                .align(Alignment.BottomCenter),
//            shape = RoundedCornerShape(12.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Black, // Customize the background color
//                contentColor = Color.White
//            )
//        ) {
//            Text(text = "Continue",
//                color = Color.White)
//        }
//    }
//}

@Composable
fun DOBSelectorScreen(sharedViewModel: SharedViewModel,
                      userDetailsViewModel: UserDetailsViewModel,
                      onNavigateToHeightScreen:()->Unit){
    var selectedDate by remember {
        mutableStateOf("")
    }
    var showDatePicker by remember { mutableStateOf(false) }
    var convertedDate by remember {
        mutableStateOf(0L)
    }
    val userData by userDetailsViewModel.userData.observeAsState()

    LaunchedEffect(userData) {
        if(userData.isNotNull()){
            selectedDate = convertMillisToDate(userData!!.dob)
        }
    }
    LaunchedEffect(selectedDate) {
        if(selectedDate.isNotBlank()){
            convertedDate = convertDateToMillis(selectedDate)
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp)
        .padding(top = 18.dp)){
        Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.yourage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        showDatePicker = false
                    })
                })

        {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { },
                label = { Text("D.O.B") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center)
                    .height(64.dp)

            )

            if (showDatePicker) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePickerModal(onDismiss = { showDatePicker = false }) {
                        selectedDate = it
                    }
                }
            }
        }
//        Text(text = "Date ${millisToAge(convertedDate)}",
//            fontSize = 40.sp)

        Button(
            onClick = {
                if(selectedDate.isNotBlank()) {
                    sharedViewModel.updateDOB(convertedDate)
                    onNavigateToHeightScreen()
                }
                      },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Customize the background color
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(id = R.string.Continue),
                color = Color.White)
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun AgeSelectionScreenPreview() {
//    DOBSelectorScreen()
//}
