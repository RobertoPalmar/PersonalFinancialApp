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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.StarSolid

@Composable
fun PreferenceStarIcon(
    modifier: Modifier = Modifier,
    size: Dp = 14.dp,
    iconColor: Color = Color(0xFFFFC107),
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = LineAwesomeIcons.StarSolid,
            contentDescription = "Preference Icon",
            tint = iconColor,
            modifier = Modifier.size(size * 0.9f)
        )
    }
}
