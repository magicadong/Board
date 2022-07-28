package com.example.board

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.board.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val model:MainViewModel by viewModels()

    var isWritable = false
    var isReadable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.model = model
        binding.clickEvents = ClickEvents()
        binding.mDrawView = binding.drawView
        binding.lifecycleOwner = this

        //检测是否有权限
        checkPermissions()
        //请求权限
        requestPermissions()
    }

    fun requestPermissions(){
        //保存需要请求的权限
        val permissionsArray = arrayListOf<String>()
        //创建请求权限的launcher对象
        val resultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            //获取请求结果
            isReadable = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            isWritable = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false


            if (isReadable){
                Log.v("pxd","获取读权限成功")
                //将视图转化为图片
                //保存图片
            }
            if (isWritable){
                Log.v("pxd","获取写权限成功")
            }
        }

        if (!isReadable){
            permissionsArray.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritable){
            permissionsArray.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        //发起请求
        resultLauncher.launch(permissionsArray.toTypedArray())
    }

    fun checkPermissions(){
        isReadable = ContextCompat.checkSelfPermission(
            this,Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ //29
            true
        }else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}