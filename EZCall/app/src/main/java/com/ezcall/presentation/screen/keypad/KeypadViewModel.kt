package com.ezcall.presentation.screen.keypad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ezcall.data.dataSource.webRTC.SignalingClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KeypadViewModel @Inject constructor():ViewModel() {

    var numberToCall by mutableStateOf("")
        private set
    fun onNewNumberToCall(newNumber :String){
        numberToCall = newNumber
    }

    fun call(){

    }

    fun handleButtonClick(text: String) {
        numberToCall += text
    }

}