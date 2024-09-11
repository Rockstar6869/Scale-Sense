package com.ujjolch.masterapp

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterapp.R

@Composable
fun GraphForSingleValue(label: String, value: Double,unit:String) {
    var showLabel by remember {
        mutableStateOf(false)
    }
    val valuesForCentring = valuesForCentring(value)
    val bottomy = valuesForCentring.first.format(2).toDouble()
    val topy = valuesForCentring.second.format(2).toDouble()
    Log.d("rfwrfefr","$topy $bottomy")
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (showLabel) {
                        showLabel = false
                    }
                })
            }
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw X-axis (right to left)
            drawLine(
                color = Color.Black,
                start = Offset(width, height*0.9f),
                end = Offset(width-(width*0.92f), height*0.9f),
                strokeWidth = 4f
            )
            // Draw Y-axis (bottom to top)
            drawLine(
                color = Color.Black,
                start = Offset(width-(width*0.92f), height*0.9f),
                end = Offset(width-(width*0.92f), height-height*0.9f),
                strokeWidth = 4f
            )

            // Draw point at the top left corner of the quadrant
//            drawCircle(
//                color = Color.Blue,
//                radius = 20f,
//                center = Offset(width * 0.5f, height * 0.5f)
//            )
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 16.sp.toPx()
                color = android.graphics.Color.BLACK  // Set text color
                textAlign = android.graphics.Paint.Align.CENTER
            }
            val paint2 = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 12.sp.toPx()
                color = android.graphics.Color.BLACK  // Set text color
                textAlign = android.graphics.Paint.Align.CENTER
            }

            // Draw text on the canvas
            drawContext.canvas.nativeCanvas.drawText(
                label,
                width * 0.5f,  // X-coordinate
                height*0.93f,  // Y-coordinate
                paint2  // Paint object
            )
            drawContext.canvas.nativeCanvas.drawText(
                "$topy",
                width-width*0.98f,  // X-coordinate
                height-height*0.9f,  // Y-coordinate
                paint2  // Paint object
            )
            drawContext.canvas.nativeCanvas.drawText(
                "$bottomy",
                width-width*0.98f,  // X-coordinate
                height*0.9f,  // Y-coordinate
                paint2  // Paint object
            )
            if(showLabel) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${value.format(2).toDouble()} $unit",
                    width * 0.5f,  // X-coordinate
                    height * 0.46f,  // Y-coordinate
                    paint  // Paint object
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(width * 0.5f, height * 0.5f),
                    end = Offset(width * 0.5f, height*0.9f),
                    strokeWidth = 6f ,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(250f, 15f), 0f)
                )
            }
        }
        ClickableCanvasCircle(width = width, height = height, onTap = {showLabel = !showLabel},showLabel)

    }
}

@Composable
fun ClickableCanvasCircle(width:Float,height:Float,onTap:()->Unit,selected:Boolean) {
    var clicked by remember { mutableStateOf(false) }
    val blue = colorResource(id = R.color.Home_Screen_Blue)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Canvas(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onTap()
                    })
                }

        ) {
            // Draw a circle
            drawCircle(
                color = if(!selected) blue else Color.Black,
                radius = 20f,
//                center = Offset(width * 0.5f, height * 0.5f)
            )
        }
    }
}