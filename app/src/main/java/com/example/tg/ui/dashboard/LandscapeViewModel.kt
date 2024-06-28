package com.example.tg.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LandscapeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Landscape Fragment"
    }
    val text: LiveData<String> = _text
}