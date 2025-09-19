package com.rpalmar.financialapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationCore : Application() {

    override fun onCreate(){
        super.onCreate();

    }
}