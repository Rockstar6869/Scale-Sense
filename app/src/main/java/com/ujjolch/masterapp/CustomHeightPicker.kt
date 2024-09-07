package com.ujjolch.masterapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CMHeightPicker(
    modifier: Modifier = Modifier,
    range: IntRange = 84..260,
    initialValue: Int = 100,
    onValueChange: (Int) -> Unit
) {
    var selected by remember {
        mutableStateOf(initialValue)
    }
    val list = range.toList()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = range.indexOf(initialValue))

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()// Adjust height for visibility of previous and next numbers
    ) {
        items(range.toList()) { number ->
            if(number in 94..250) {
                selected = if((listState.firstVisibleItemIndex)>=8) listState.firstVisibleItemIndex+3  else 10
            }
            androidx.compose.material3.Text(text = "")  //This is for creating an empty gat after 100 and before 10
            if(number in 94..250) {
                NumberItem2(
                    number = number,
                    selected = list[selected] == number,
                    onClick = { onValueChange(selected) }
                )
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        onValueChange(list[selected])
    }
}

@Composable
fun NumberItem2(number: Int, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
//            .clickable(onClick = onClick)
        ,
        contentAlignment = Alignment.Center
    ) {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start){
            Spacer(modifier = Modifier.fillMaxWidth(0.45f))
            androidx.compose.material3.Text(
                text = buildAnnotatedString {
                    append(number.toString())
                    if (selected) {
                        withStyle(style = SpanStyle(fontSize = 20.sp)) {
                            append(" cm")
                        }
                    }
                },
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Thin,
                fontSize = 30.sp // Default font size for the number
            )
        }
    }
}

@Composable
fun InHeightPicker(
    modifier: Modifier = Modifier,
    list: List<String> ,
    initialValue: String = "3'4\"",
    onValueChange: (String) -> Unit
) {
    var selected by remember {
        mutableStateOf(0)
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = list.indexOf(initialValue)-3)
    LaunchedEffect(initialValue) {
        selected = list.indexOf(initialValue)
        listState.scrollToItem(selected - 3)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()// Adjust height for visibility of previous and next numbers
    ) {
        itemsIndexed(list) { index,number ->
            if(index in 0..64) {
                selected = remember { derivedStateOf { listState.firstVisibleItemIndex } }.value+3
            }
            androidx.compose.material3.Text(text = "")  //This is for creating an empty gat after 100 and before 10
            if(index in 0..65) {
                NumberItem3(
                    number = number,
                    selected = list[selected] == number,
                    onClick = {  }
                )
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        onValueChange(list[selected])
    }
}

@Composable
fun NumberItem3(number: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
//            .clickable(onClick = onClick)
        ,
        contentAlignment = Alignment.Center
    ) {
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start){
            Spacer(modifier = Modifier.fillMaxWidth(0.45f))
            androidx.compose.material3.Text(
                text = buildAnnotatedString {
                    append(number)
//                    if (selected) {
//                        withStyle(style = SpanStyle(fontSize = 20.sp)) {
//                            append("")
//                        }
//                    }
                },
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Thin,
                fontSize = 30.sp // Default font size for the number
            )
        }
    }
}

