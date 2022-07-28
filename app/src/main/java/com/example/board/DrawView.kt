package com.example.board

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawView: View {
    var mColor = Color.BLACK
        set(value) {
            field = value
            mPaint.color = value
        }
    var mStrokeWith = this.context.dp2px(5)
        set(value) {
            field = value
            Log.v("pxd","width:$value")
            mPaint.strokeWidth = value
        }

    var mPath:Path?= Path()

    var mPaint = Paint().apply {
        color = mColor
        strokeWidth = mStrokeWith
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    val pathModelLists = arrayListOf<PathModel>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        pathModelLists.forEach { model ->
            canvas?.drawPath(model.path,model.paint)
        }

        mPath?.let {
            canvas?.drawPath(mPath!!,mPaint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                //创建新的path
                mPath = Path()
                //确定落点
                mPath!!.moveTo(event.x,event.y)
            }
            MotionEvent.ACTION_MOVE ->{
                //从path的最后一个点到当前点画一条线
                mPath!!.lineTo(event.x,event.y)
                //刷新
                invalidate()
            }
            MotionEvent.ACTION_UP ->{
                //保存当前的路劲
                val paint = Paint().apply {
                    color = mColor
                    strokeWidth = mStrokeWith
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                }
                pathModelLists.add(PathModel(paint,mPath!!))

                mPath = null
            }
        }
        return true
    }

    fun undo(){
        if (pathModelLists.size > 0){
            pathModelLists.removeLast()
            invalidate()
        }
    }
}