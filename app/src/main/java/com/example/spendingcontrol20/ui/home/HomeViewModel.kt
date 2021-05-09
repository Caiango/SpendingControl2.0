package com.example.spendingcontrol20.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendingcontrol20.model.FireStoreUtils

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Home Fragment"
    }
    val text: LiveData<String> = _text

    val saldoMensal = FireStoreUtils.saldoSubMensal

    val saldoProgDesp = FireStoreUtils.saldoProgDesp

    val saldoProgGain = FireStoreUtils.saldoProgGain

    val saldoTotal = FireStoreUtils.saldoSubTotal

}