package com.example.board

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.FileNotFoundException
import java.io.OutputStream
import java.util.*

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

    fun savePictureToGallary(view: View, drawView: DrawView, model: MainViewModel){
        //检测是否有权限
        val readPermission = view.context.checkReadablePermission()
        if (readPermission){
            //将视图转化为图片
            val bitmap = convertViewToBitmap(drawView)
            //保存图片
            saveBitmap(bitmap,drawView){ uri ->
                if (uri != null){
                    model.imageUri = uri
                    Toast.makeText(view.context,"save ok",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(view.context,"save failed",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(view.context,"no permission", Toast.LENGTH_LONG).show()
        }
    }

    fun shareImage(view: View, model: MainViewModel){
        if (model.imageUri != null) {
            //隐式跳转
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, model.imageUri)
                putExtra(Intent.EXTRA_TEXT, "我的靓照")
                type = "image/jpg"
            }
            view.context.startActivity(intent)
        }else{
            Toast.makeText(view.context,"no image uri", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 相册读取图片
     * 相机拍照
     * 相册读取视频  播放视频
     * 相机拍视频
     */

    //将图片保存到相册中
    private fun saveBitmap(bitmap: Bitmap, view: DrawView, callBack:(Uri?)->Unit){
        //获取view所在的activity -> ContextWrapper(contentResolver)
        val activity = view.context as MainActivity

        //图片所在的文件夹的路径
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        //配置图片信息
        val contentValues = ContentValues().apply {
            //用时间作为文件名
            put(MediaStore.Images.Media.DISPLAY_NAME, getName())
            //图片格式
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpg")
            //宽度
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            //高度
            put(MediaStore.Images.Media.HEIGHT,bitmap.height)
        }

        //定位到这个图片的文件路径
        val imageUri = activity.contentResolver.insert(uri, contentValues)


        if (imageUri != null) {
            //创建输出流
            activity.contentResolver.openOutputStream(imageUri).use { os ->
                //通过输出流将bitmap写出
                val result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,os)
                if (result){
                    callBack(imageUri)
                }else{
                    callBack(null)
                }
            }

//            var imageOutputStream: OutputStream? = null
//            try {
//                //创建输出流
//                imageOutputStream = activity.contentResolver.openOutputStream(imageUri)
//                //通过输出流将bitmap写出
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,imageOutputStream)
//            } catch (e: Exception){
//                e.printStackTrace()
//            } finally {
//                imageOutputStream?.close()
//            }

        }else{
            throw NullPointerException("获取路径失败")
        }
    }

    //将时间转化为名字
    private fun getName():String{
        //2022/7/29
        //val str = DateFormat.getDateFormat(view.context).format(Date())

        val timeStr = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$timeStr.jpg"
    }

    //将视图转化为图片
    //将view的内容重新绘制到我们提供的bitmap
    private fun convertViewToBitmap(view: DrawView):Bitmap{
        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.MAGENTA)
        view.draw(canvas)
        return bitmap
    }
}









