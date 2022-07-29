package com.example.board

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var showPenSize = MutableLiveData(false)
    var isExpanded = MutableLiveData(false)
    var imageUri:Uri? = null
}