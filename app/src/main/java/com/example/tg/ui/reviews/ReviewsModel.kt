package com.example.tg.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReviewsModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Reviews Fragment"
    }
    val text: LiveData<String> = _text
}