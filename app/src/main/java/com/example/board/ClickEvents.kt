package com.example.board

import android.graphics.Color
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClickEvents {
    fun changePenSizeVisibleState(view:View, model: MainViewModel){
        if (model.showPenSize.value == true){
            model.showPenSize.postValue(false)
        }else{
            model.showPenSize.postValue(true)
        }
    }

    fun changeColorPalletExpandableState(view:View, model: MainViewModel){
        if (model.isExpanded.value == true){
            model.isExpanded.postValue(false)
        }else{
            model.isExpanded.postValue(true)
        }
    }

    fun changePaintColor(view:View, drawView: DrawView){
        //获取tag值
        val index = (view.tag as String).toInt() //1 2 3 4 5
        //取出对应的颜色
        val colorString = colorsArray[index-1]
        //更改画笔颜色
        drawView.mColor = Color.parseColor(colorString)
    }

    fun changeStrokeWidth(view: View, drawView: DrawView){
        //获取tag值
        val widthDp = (view.tag as String).toInt()
        val widthPixel = view.context.dp2px(widthDp)

        Log.v("pxd","changeWidth: $widthPixel  $widthDp")
        drawView.mStrokeWith = widthPixel
    }

    fun undo(view: View, drawView: DrawView){
        drawView.undo()
    }
}