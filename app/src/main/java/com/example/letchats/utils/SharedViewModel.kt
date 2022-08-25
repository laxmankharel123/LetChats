package com.example.letchats.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.schedule

class SharedViewModel : ViewModel(){

    val openMainAct = MutableLiveData<Boolean>()

    private var timer: TimerTask? = null

    fun onFromSplash() {
        if (timer == null) {
            timer = Timer().schedule(2000) {
                openMainAct.postValue(true)
            }
        }

    }
}