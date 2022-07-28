package com.example.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var showPenSize = MutableLiveData(false)
    var isExpanded = MutableLiveData(false)
}