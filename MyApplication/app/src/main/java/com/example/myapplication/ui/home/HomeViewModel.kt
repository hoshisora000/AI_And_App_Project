package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "維修中"
    }

    var USER_ID = "O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6"
    val text: LiveData<String> = _text

}