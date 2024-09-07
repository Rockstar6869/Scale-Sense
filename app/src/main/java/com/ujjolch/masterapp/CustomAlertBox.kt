package com.ujjolch.masterapp


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlin.math.abs

// Alert box for Home Screen when weight difference is more than 3 kgs

@Composable
fun AlertBox(difference:Double,onYesClick:()->Unit) {
    val formattedDifference = abs(difference.format(2).toDouble())
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button
                showDialog = false
            },
            title = {
                Text(text = "Is this you?",
                    fontWeight = FontWeight.Bold)
            },
            text = {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        buildAnnotatedString {
                            append("The weight that was measured has a difference of ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$formattedDifference Kgs")
                            }
                            append(" with your previous weight.If this is not you then please click on ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("No ")
                            }
                            append("because saving this data can impact your history graph.")
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClick()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Yes",color=Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Handle the dismiss button click here
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("No", color = Color.White)
                }
            }
        )
    }
}