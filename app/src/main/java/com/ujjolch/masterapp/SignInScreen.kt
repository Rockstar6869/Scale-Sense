package com.ujjolch.masterapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R
import kotlinx.coroutines.delay

//@Composable
//fun SignUpScreen(OnNavigateToLogIn:() -> Unit,
//                 authViewModel: AuthViewModel
//){
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//
//    Column (
//        Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally){
//        OutlinedTextField(value = email,
//            onValueChange = {email=it},
//            label = { Text("Email") },
//            modifier = Modifier.padding(8.dp))
//        OutlinedTextField(
//            value = password,
//            onValueChange = {password = it},
//            label = { Text("Password") },
//            modifier = Modifier.padding(8.dp),
//            visualTransformation = PasswordVisualTransformation())
//        OutlinedTextField(value = firstName,
//            onValueChange = {firstName=it},
//            label = { Text("First Name") },
//            modifier = Modifier.padding(8.dp))
//        OutlinedTextField(value = lastName,
//            onValueChange = {lastName=it},
//            label = { Text("Last Name") },
//            modifier = Modifier.padding(8.dp))
//        Button(onClick = {
//            authViewModel.signUp(email,password,firstName,lastName)
//            email=""
//            password=""
//            firstName=""
//            lastName=""
//        }) {
//            Text(text = "Sign In")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Already have an account? Sign in.",
//            modifier = Modifier.clickable { OnNavigateToLogIn() }
//        )
//    }
//}

@Composable
fun SignUpScreen(OnNavigateToLogIn:() -> Unit,
                 authViewModel: AuthViewModel,
                 signInSharedViewModel: SignInSharedViewModel,
                 onSignInSuccess:()->Unit,
                 onNavigateToPrivacyPolicy:()->Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val result by authViewModel.authResult.observeAsState()
    var signinclick by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val cachedfname by signInSharedViewModel.Fname.observeAsState()
    val cachedLname by signInSharedViewModel.Lname.observeAsState()
    val cachedemail by signInSharedViewModel._SignInEmail.observeAsState()
    val cachedpassword by signInSharedViewModel.Password.observeAsState()
    val cachedcinfirmpassword by signInSharedViewModel.ConfirmPassword.observeAsState()

    var Agreed by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val homeScreenBlue by remember {
        mutableStateOf( R.color.Home_Screen_Blue)
    }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageSizeForLargeScreen = (screenHeight.value/3.054).dp



    LaunchedEffect(signinclick) {
        if(signinclick){
            loading = true
            while(loading) {
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(
                            context,
                            "Account Created!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onSignInSuccess()
                        loading = false
                        signinclick = false
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "${(result as Result.Error).exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        signinclick = false
                        authViewModel.resetAuthResult()
                    }

                    else -> {}
                }
                delay(5)
            }

        }
    }

    LaunchedEffect(cachedfname,cachedLname,cachedemail,cachedpassword) {
        if(cachedfname.isNotNull() && cachedfname!!.isNotBlank()){
            firstName = cachedfname!!
        }
        if(cachedLname.isNotNull() && cachedLname!!.isNotBlank()){
            lastName = cachedLname!!
        }
        if(cachedemail.isNotNull() && cachedemail!!.isNotBlank()){
            email = cachedemail!!
        }
        if(cachedpassword.isNotNull() && cachedpassword!!.isNotBlank()){
            password = cachedpassword!!
        }
        if(cachedcinfirmpassword.isNotNull() && cachedcinfirmpassword!!.isNotBlank()){
            confirmPassword = cachedcinfirmpassword!!
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .background(Color.White)
            .clickable {
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.health_bliss_logo_final_1),
                contentDescription = "Company Logo",
                modifier = Modifier.size(if(screenHeight>700.dp) imageSizeForLargeScreen else 150.dp)
                    .padding(if(screenHeight>700.dp) 25.dp else 0.dp),
                contentScale = ContentScale.Fit
            )
        }
            Column (Modifier.align(Alignment.Center)){
//            Spacer(modifier = Modifier.height(60.dp))
            TransparentTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
//            Spacer(modifier = Modifier.height(8.dp))
            TransparentTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            TransparentTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            TransparentTextFieldForPassword(
                value = password,
                onValueChange = { password = it },
                label = "Password(Minimum 6 characters)"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                TransparentTextFieldForPassword(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password"
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
                }
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Checkbox(
                    checked = Agreed,
                    onCheckedChange = { Agreed = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green , // Change color when checked
                        uncheckedColor = if (Agreed) Color.Green else Color.Red, // Change color when unchecked
                        checkmarkColor = Color.White, // Color of the checkmark
                    )
                )
                Text(
                    text = "I have read and agreed on ",
                    color = Color.Black
                )
                Text(
                    text = "Privacy Policy",
                    color = colorResource(id = homeScreenBlue),
                    modifier = Modifier.clickable {
                            signInSharedViewModel.set(firstName,lastName,email,password,confirmPassword)
                            onNavigateToPrivacyPolicy()
                    }
                )
            }
            Button(
                enabled = Agreed,
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() && confirmPassword.isNotBlank()){
                        if(password == confirmPassword) {
                            authViewModel.signUp(email, password, firstName, lastName)
                            signinclick = true
                            email = ""
                            password = ""
                            confirmPassword = ""
                            firstName = ""
                            lastName = ""
                            signInSharedViewModel.delete()
                        }
                        else{
                            ToastManager.showToast(context,"Password does not match",Toast.LENGTH_SHORT)
                        }
                }
                          },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        Text("Already have an account? Sign In.",
            modifier = Modifier
                .clickable {
                    signInSharedViewModel.delete()
                    OnNavigateToLogIn()
                }
                .align(Alignment.CenterHorizontally), color = colorResource(id = homeScreenBlue))
            Spacer(modifier = Modifier.height(16.dp))
            if(loading){
                LinearProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextFieldForPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    if (passwordVisible)
                        Icon(painter = painterResource(id = R.drawable.baseline_visibility_24), contentDescription = "Visible")
                    else
                        Icon(painter = painterResource(id = R.drawable.baseline_visibility_off_24), contentDescription = "Visible off")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
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
fun TextFieldPreview() {
    TransparentTextFieldForPassword(value = "dfbdgd", onValueChange = {}, label = "Password")
}
