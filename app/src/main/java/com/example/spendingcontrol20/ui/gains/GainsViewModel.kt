package com.example.spendingcontrol20.ui.gains

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendingcontrol20.firestore.FireStoreUtils
import com.example.spendingcontrol20.utils.Constants

class GainsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }

    val text: LiveData<String> = _text

    private var _data = MutableLiveData<ArrayList<HashMap<String, String>>>().apply {
        value = Constants.LIST
    }

    val lista: LiveData<ArrayList<HashMap<String, String>>> = _data


}