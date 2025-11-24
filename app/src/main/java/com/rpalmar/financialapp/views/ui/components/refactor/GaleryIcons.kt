package com.rpalmar.financialapp.views.ui.components.refactor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun CreditCardIcon(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box {
            // Círculo rojo
            Box(
                modifier = Modifier
                    .size(size * 0.6f)
                    .offset(x = (-size * 0.15f))
                    .background(Color(0xFFFF4949), shape = CircleShape)
            )

            // Círculo amarillo
            Box(
                modifier = Modifier
                    .size(size * 0.6f)
                    .offset(x = (size * 0.15f))
                    .background(Color(0xFFFFC700), shape = CircleShape)
            )
        }
    }
}