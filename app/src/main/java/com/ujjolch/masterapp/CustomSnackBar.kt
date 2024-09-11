package com.ujjolch.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.masterapp.R
import kotlinx.coroutines.launch

@Composable
fun CustomSnackbar(message:String,onStartClick:()->Unit,showSnackBar:Boolean) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Trigger the snackbar
    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry",
                withDismissAction = true
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }


    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier,
                containerColor = colorResource(id = R.color.SnackBar_Color),
                action = {
                    TextButton(
                        onClick = { onStartClick() }
                    ) {
                        Text(
                            text = "Start",
                            color = Color.White
                        )
                    }
                },
                content = {
                    Text(
                        text = message,
                        color = Color.White
                    )
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SnackBarPreview(){
    CustomSnackbar(message = "Bluetooth is disabled", showSnackBar = true, onStartClick = {})
}
