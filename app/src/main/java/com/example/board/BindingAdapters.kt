package com.example.board

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("shouldOpen")
fun View.shouldOpen(state:Boolean){
    if (state){
        ObjectAnimator.ofFloat(
            this,
            "translationY",
            0f,
            -height.toFloat()
        ).apply {
            duration = 300
            interpolator = LinearInterpolator()
            start()
        }
    }else{
        ObjectAnimator.ofFloat(
            this,
            "translationY",
            0f
        ).apply {
            duration = 300
            interpolator = LinearInterpolator()
            start()
        }
    }
}

@BindingAdapter("shouldExpand")
fun FloatingActionButton.shouldExpand(state: Boolean){
    val index = (tag as String).toInt()
    val space = context.dp2px(20)
    if (state){
        this.animate()
            .translationYBy(-index*(space+height))
            .setDuration(400)
            .setInterpolator(BounceInterpolator())
            .start()
    }else{
        this.animate()
            .translationY(0f)
            .setDuration(400)
            .setInterpolator(BounceInterpolator())
            .start()
    }
}