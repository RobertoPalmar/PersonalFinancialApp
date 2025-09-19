package com.rpalmar.financialapp.views.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.navigation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.jvm.java

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
    goToNextActivity: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        delay(3000L);
        goToNextActivity()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    contentDescription = "Splash Logo Icon",
                    painter = painterResource(id = R.drawable.ic_financial),
                    modifier = Modifier.fillMaxSize(0.3f)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
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