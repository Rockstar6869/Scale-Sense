package com.ujjolch.masterapp

import android.widget.Toast
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.masterapp.R

@Composable
fun ForgetPasswordScreen(onNavigateBack:()->Unit,authViewModel: AuthViewModel= viewModel()) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text(text = "Forget Password") },
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
    Box(
        Modifier
            .fillMaxSize()
            .padding(it)
            .pointerInput(Unit){
                detectTapGestures {
                    keyboardController?.hide()
                }
            }) {
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Reset Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Enter the email address associated with your account and we will send an email with instructions to reset your password")
                Spacer(modifier = Modifier.height(20.dp))
                TransparentTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (email.isNotBlank()) {
                authViewModel.resetPassword(
                    email = email,
                    onSuccess = {
                        Toast.makeText(context,"Password reset email sent",Toast.LENGTH_LONG).show()
                        onNavigateBack()
                    },
                    onFailure = {
                        Toast.makeText(context, it,Toast.LENGTH_LONG).show()
                    }
                )
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // Customize the background color
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Send Instructions")
                }
            }

        }
    }
}
)
}

@Preview(showBackground = true)
@Composable
fun ForgetPasswordScreenPreview() {
    ForgetPasswordScreen({})
}
