package com.example.masterapp

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavController,
               authViewModel: AuthViewModel,
               bluetoothViewModel: BleScanViewModel = viewModel(),
               userDetailsViewModel: UserDetailsViewModel = viewModel()
){
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    var StartupDone by remember {
        mutableStateOf(false)
    }
    var startDestination by remember {
        mutableStateOf(Screen.IntroScreen.route)
    }
    var signinclick by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        userDetailsViewModel.gethistlist()
    }

    LaunchedEffect(isLoading, isLoggedIn) {
        if (!isLoading && !StartupDone) {
            if (isLoggedIn) {
                    startDestination = Screen.MainView.route
                    StartupDone =true

            } else {
                    startDestination = Screen.LogInScreen.route
                    StartupDone = true
            }
        }
    }
    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination
//        if(isLoading) Screen.IntroScreen.route else{
//        if(isLoggedIn)
//            Screen.MainView.route else Screen.LogInScreen.route}
    ) {
        composable(Screen.IntroScreen.route){
            IntroScreen()
        }
        composable(Screen.SignInScreen.route) {
            BackHandler {

            }
            SignUpScreen(authViewModel = authViewModel,
                OnNavigateToLogIn = {
                    navController.navigate(Screen.LogInScreen.route)
                },
                onSignInSuccess = { navController.navigate(Screen.UpdateDetailScreen.route)})
        }
        composable(Screen.LogInScreen.route) {
            BackHandler {

            }
            LoginScreen(authViewModel = authViewModel,
                onNavigateTosignin = {
                    navController.navigate(Screen.SignInScreen.route)
                },
                onLogInSuccess = { navController.navigate(Screen.MainView.route) })
        }
        composable(Screen.UpdateDetailScreen.route) {
            BackHandler {

            }
            UpdateDetailScreen(onNavigateToAddDevice = {
                navController.navigate(Screen.AddDeviceScreen.route)
            })
        }
        composable(Screen.AddDeviceScreen.route) {
            AddDevice( onNavigateToMainView = {
                navController.navigate(Screen.MainView.route)
            })
        }
        composable(Screen.MainView.route) {
            MainView(authViewModel,
                onLogOutSuccess = {navController.navigate(Screen.LogInScreen.route)})
        }
    }
}