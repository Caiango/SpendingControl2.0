package com.example.spendingcontrol20.ui.spending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpendingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is spending Fragment"
    }
    val text: LiveData<String> = _text
}