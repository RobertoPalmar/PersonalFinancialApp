package com.rpalmar.financialapp.views.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        setContent{

        }
    }
}

