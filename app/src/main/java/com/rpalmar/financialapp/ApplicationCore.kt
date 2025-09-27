package com.rpalmar.financialapp

import android.app.Application
import com.rpalmar.financialapp.usecases.SetBaseRoomSeed
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ApplicationCore : Application() {

    @Inject
    lateinit var setBaseRoomSeed: SetBaseRoomSeed

    override fun onCreate(){
        super.onCreate();
        CoroutineScope(Dispatchers.IO).launch {
            setBaseRoomSeed()
        }
    }
}