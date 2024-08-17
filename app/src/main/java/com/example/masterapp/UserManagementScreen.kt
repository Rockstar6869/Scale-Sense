package com.example.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull

@Composable
fun UserManagementScreen(userDetailsViewModel: UserDetailsViewModel,
                         onNavigateBack:()->Unit,
                         onNavigateToAddUser:()->Unit) {
    val subUserList by userDetailsViewModel.SubUserList.observeAsState()
    val currentSubUser by userDetailsViewModel.currentSubUser.observeAsState()
    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material.Text(
                            text = "User Management",
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { onNavigateBack() }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp
            )
        },
        content = {
            if (subUserList.isNotNull()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = colorResource(id = R.color.Home_Screen_White))
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(subUserList!!) {
                            val userInitials = "${it.firstName.get(0)}${it.lastName.get(0)}"
                            UserManagementItem(
                                enabled = currentSubUser!!.uuid == it.uuid,
                                text = it.firstName,
                                iconLabel = userInitials.uppercase(),
                                onSelectUser = {
                                    userDetailsViewModel.clearUserDataAndHist()
                                    userDetailsViewModel.setCurrentSubUser(it)
                                }
                            )
                        }
                        item {
                            BoxWithAddIcon {
                                onNavigateToAddUser()
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserManagementItem(enabled: Boolean,
                       text: String,
                       iconLabel:String,
                       onSelectUser:()->Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onSelectUser()
                })
            }
            .border(
                width = if (enabled) 2.dp else 0.dp,
                color = if (enabled) colorResource(id = R.color.Home_Screen_Blue) else Color.Transparent
            )
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(onClick = { onSelectUser() },
                containerColor = Color.Gray) {
                Text(text = iconLabel,
                    color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun BoxWithAddIcon(onAddUser: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onAddUser()
                })
            }
            .border(
                width = 0.dp,
                color = Color.Transparent
            )
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = { onAddUser() },
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}