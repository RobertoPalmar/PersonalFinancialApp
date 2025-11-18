package com.rpalmar.financialapp.views.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.navigation.MainActivity
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.Grey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        setContent {
            SplashScreen(
                goToNextActivity = { goToNextActivity() },
                syncAction = { splashViewModel.syncExchangeRates() }
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
    goToNextActivity: () -> Unit = {},
    syncAction: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        syncAction()
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


            Box(
                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color = DarkGrey,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Text(
                        text = "Updating Data",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                }
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