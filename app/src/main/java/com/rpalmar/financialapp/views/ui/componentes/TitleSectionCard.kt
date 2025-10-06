package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TitleSectionCard(
    title: String,
    subtitle: String? = null,
    backgroundColor: Color,
    icon: ImageVector? = null
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(backgroundColor),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        )
    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = subtitle,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}