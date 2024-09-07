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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AgePicker(
    modifier: Modifier = Modifier,
    range: IntRange = 0..110,
    initialValue: Int = 0,
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
            if(number in 10..100) {
                selected = if((listState.firstVisibleItemIndex + 3)>=10) listState.firstVisibleItemIndex + 3 else 10
            }
            Text(text = "")  //This is for creating an empty gat after 100 and before 10
            if(number in 10..100) {
                NumberItem(
                    number = number,
                    selected = selected == number,
                    onClick = { onValueChange(number) }
                )
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        onValueChange(list[selected])
    }
}

@Composable
fun NumberItem(number: Int, selected: Boolean, onClick: () -> Unit) {
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
            Text(
                text = buildAnnotatedString {
                    append(number.toString())
                    if (selected) {
                        withStyle(style = SpanStyle(fontSize = 20.sp)) {
                            append(" years old")
                        }
                    }
                },
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Thin,
                fontSize = 30.sp // Default font size for the number
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AgePickerPreview() {
    AgePicker(
        range = 0..50,
        initialValue = 25,
        onValueChange = { selectedNumber ->
            // Handle the selected number here if needed
            println("Selected number: $selectedNumber")
        }
    )
}