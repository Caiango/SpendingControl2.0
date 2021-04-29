package com.example.spendingcontrol20.ui.spending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendingcontrol20.model.FireStoreUtils

class SpendingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is spending Fragment"
    }

    val text: LiveData<String> = _text

    val lista = FireStoreUtils.itemList

    val saldoText = FireStoreUtils.saldoDesp

    val saldoMensal = FireStoreUtils.saldoMensalDesp

    val saldoProg = FireStoreUtils.saldoProgDesp

    val saldoSubProg = FireStoreUtils.saldoSubProgDesp

}