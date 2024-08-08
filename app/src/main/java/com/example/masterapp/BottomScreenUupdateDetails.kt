package com.example.masterapp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BottomScreenUpdateDetails(onNavigateToMeScreen:()->Unit,
                              userDetailsViewModel: UserDetailsViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp)
                        , horizontalArrangement = Arrangement.Center){
                        Text(text = "Update Details",
                            color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToMeScreen()}) {
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
        UpdateDetailScreen(userDetailsViewModel) {
            onNavigateToMeScreen()
        }
    }
}
)
}