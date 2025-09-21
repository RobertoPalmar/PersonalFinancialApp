package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp

@Composable
fun MainLayout(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(25.dp, 15.dp)
    ) {
        content()
    }
}