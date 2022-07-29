package com.example.board

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.dp2px(dp:Int) = resources.displayMetrics.density*dp

val colorsArray = arrayOf(
    "#067063","#FF4500","#AC00B6","#0096FD","#FF0061","#ffffff"
)

//权限检测
fun Context.checkReadablePermission():Boolean{
    val result = ContextCompat.checkSelfPermission(
        this,Manifest.permission.READ_EXTERNAL_STORAGE)
    return result == PackageManager.PERMISSION_GRANTED
}