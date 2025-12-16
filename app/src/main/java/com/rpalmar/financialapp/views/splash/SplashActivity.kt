package com.rpalmar.financialapp.views.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rpalmar.financialapp.views.AppViewModel
import com.rpalmar.financialapp.views.navigation.MainActivity
import com.rpalmar.financialapp.views.ui.components.CreditCardIcon
import com.rpalmar.financialapp.views.ui.theme.Black
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        setContent {
            SplashScreen(
                goToNextActivity = { goToNextActivity() }
            )
        }

    }

    private fun goToNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish()
    }
}

@Composable
fun SplashScreen(
    appViewModel: AppViewModel = hiltViewModel(),
    goToNextActivity: () -> Unit = {},
) {

    //INITIALIZATION
    val isInitialized by appViewModel.isAppInitialized.collectAsState()

    LaunchedEffect(isInitialized) {
        if (isInitialized) {
            delay(1000)
            goToNextActivity()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CreditCardIcon(size = 150.dp)
                Spacer(modifier = Modifier.height(10.dp))
            }


//            Box(
//                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(10.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    CircularProgressIndicator(
//                        color = DarkGrey,
//                        strokeWidth = 2.dp,
//                        modifier = Modifier
//                            .size(20.dp)
//                    )
//                }
//            }

        }
    }
}

//PREVIEW FUNCTION
@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    SplashScreen(
        goToNextActivity = {}
    )
}