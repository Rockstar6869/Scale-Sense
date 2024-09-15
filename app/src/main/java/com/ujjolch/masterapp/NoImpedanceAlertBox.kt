package com.ujjolch.masterapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun NoImpedanceAlertBox(onDismiss:()->Unit) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button
                showDialog = false
            },
            title = {
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    Text(
                        text = "Tips",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(Modifier.padding(16.dp)) {
                    Text(text = "1. No body fat detected. Please try again.")

                    Text(text = "2. For accurate measurements, stand barefoot with your" +
                            " legs naturally separated. Ensure that no socks or footwear " +
                            "are covering the electrodes on either side.")

                    Text(text = "3. Place the scale on a hard, flat surface " +
                            "for the most accurate results.")

                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Ok",color= Color.White)
                }
            },
//            dismissButton = {
//                Button(
//                    onClick = {
//                        // Handle the dismiss button click here
//                        showDialog = false
//                    },
//                    colors = ButtonDefaults.buttonColors(Color.Black)
//                ) {
//                    Text("No", color = Color.White)
//                }
//            }
        )
    }
}

@Preview (showBackground = true)
@Composable
fun NoImpedanceAlertBox(){
    NoImpedanceAlertBox {

    }
}