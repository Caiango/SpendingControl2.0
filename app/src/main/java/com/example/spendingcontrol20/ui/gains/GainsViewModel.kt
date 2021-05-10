package com.example.spendingcontrol20.ui.gains

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendingcontrol20.model.FireStoreUtils

class GainsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }

    val text: LiveData<String> = _text

    val lista = FireStoreUtils.itemList

    val saldoText = FireStoreUtils.saldoGain

    val saldoMensal = FireStoreUtils.saldoMensalGain

    val saldoProg = FireStoreUtils.saldoProgGain



}