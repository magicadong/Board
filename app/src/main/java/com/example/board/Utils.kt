package com.example.board

import android.content.Context

fun Context.dp2px(dp:Int) = resources.displayMetrics.density*dp

val colorsArray = arrayOf(
    "#067063","#FF4500","#AC00B6","#0096FD","#FF0061","#ffffff"
)