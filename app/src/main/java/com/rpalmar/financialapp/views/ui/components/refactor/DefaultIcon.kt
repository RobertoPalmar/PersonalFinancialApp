package com.rpalmar.financialapp.views.ui.components.refactor

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.views.ui.theme.Black

@Composable
fun DefaultIcon(
    title: String,
    icon: ImageVector? = null,
    textIcon: String? = null,
    color: Color,
    circleSize: Dp,
    iconSize: Dp = circleSize * 0.6f,
    textColor: Color = Black
) {
    Box(
        modifier = Modifier
            .size(circleSize)
            .background(color = color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            icon != null -> {
                //USE ICON
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = rememberVectorPainter(icon),
                    contentDescription = title,
                    tint = textColor
                )
            }

            !textIcon.isNullOrBlank() -> {
                //SHOW TEXT
                androidx.compose.material3.Text(
                    text = textIcon,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = (iconSize.value * 0.8).sp
                )
            }

            else -> {
                //PLACEHOLDER
                androidx.compose.material3.Text(
                    text = "?",
                    color = textColor,
                    fontSize = (iconSize.value * 0.8).sp
                )
            }
        }
    }
}
