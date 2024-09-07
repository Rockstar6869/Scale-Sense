package com.ujjolch.masterapp

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.yml.charts.common.extensions.isNotNull

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
    val context = LocalContext.current
    val currentLang = getSavedLanguage(context)
    LaunchedEffect(currentLang) {
        if(currentLang.isNotNull()){
            setLocale(context,currentLang!!)
        }
    }
    LaunchedEffect(true) {
        userDetailsViewModel.gethistlist()
        userDetailsViewModel.updateUserData()
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
                onSignInSuccess = { navController.navigate(Screen.MainViewForUpdateDetails.route)},
                onNavigateToPrivacyPolicy = {navController.navigate(Screen.PrivacyPolicyScreen.route)})
        }
        composable(Screen.LogInScreen.route) {
            BackHandler {

            }
            LoginScreen(authViewModel = authViewModel,
                onNavigateTosignin = {
                    navController.navigate(Screen.SignInScreen.route)
                },
                onLogInSuccess = { navController.navigate(Screen.MainView.route) },
                onNavigateToPrivacyPolicy = { navController.navigate(Screen.PrivacyPolicyScreen.route) })
        }
//        composable(Screen.UpdateDetailScreen.route) {
//            BackHandler {
//
//            }
//            UpdateDetailScreen(onNavigateToAddDevice = {
//                navController.navigate(Screen.AddDeviceScreen.route)
//            },
//                showUnitChanger = true)
//        }
        composable(Screen.MainViewForUpdateDetails.route){
            BackHandler {

            }
            MainViewForUpdateDetails(
                showBackButton = false,
                onNavigateBack = {},
                onSave = {navController.navigate(Screen.AddDeviceNewNavigation.route)})
        }
//        composable(Screen.AddDeviceScreen.route) {
//            AddDevice( onNavigateToMainView = {
//                navController.navigate(Screen.MainView.route)
//            })
//        }
        composable(Screen.AddDeviceNewNavigation.route) {
            BackHandler {

            }
            AddDeviceNewNavigation(
                onNavigateToMeScreen = { navController.navigate(Screen.MainView.route)},
                backButtonEnabled = false
                )
        }
        composable(Screen.MainView.route) {
            MainView(authViewModel,
                onLogOutSuccess = {navController.navigate(Screen.LogInScreen.route)})
        }
        composable(Screen.PrivacyPolicyScreen.route) {
            // Check the previous destination
            val previousDestination: NavDestination? =
                navController.previousBackStackEntry?.destination
            PrivacyPolicyScreen {
                if (previousDestination.isNotNull()){
                    if (previousDestination!!.route == Screen.LogInScreen.route) {
                        navController.navigate(Screen.LogInScreen.route)
                    } else if (previousDestination!!.route == Screen.SignInScreen.route) {
                        navController.navigate(Screen.SignInScreen.route)
                    }
            }
            }
        }
    }
}