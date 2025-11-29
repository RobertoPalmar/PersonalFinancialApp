package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rpalmar.financialapp.views.ui.theme.Black

@Composable
fun MainLayout(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Black)
            .fillMaxSize(1f)
    ) {
        content()
    }
}