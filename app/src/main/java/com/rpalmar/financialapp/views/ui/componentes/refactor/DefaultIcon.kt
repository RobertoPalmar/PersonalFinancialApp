package com.rpalmar.financialapp.views.ui.componentes.refactor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import com.rpalmar.financialapp.views.ui.theme.Black

@Composable
fun DefaultIcon(
    title:String,
    icon: ImageVector,
    color: Color,
    circleSize: Dp,
    iconSize: Dp = circleSize * 0.8f
) {
    //ICON
    Box(
        modifier = Modifier
            .size(circleSize)
            .background(color = color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = rememberVectorPainter(image = icon),
            contentDescription = title,
            tint = Black
        )
    }
}