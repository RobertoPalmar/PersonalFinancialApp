package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.views.ui.theme.Blue

@Composable
fun SimpleButton(
    onClick: () -> Unit,
    text: String? = null,
    icon: ImageVector? = null,
    color: Color,
    type: ButtonType = ButtonType.PRIMARY,
) {
    val width = 110.dp;
    val primaryColor = when(type){
        ButtonType.PRIMARY -> color
        ButtonType.SECONDARY -> Color.White
        ButtonType.OUTLINE -> Color.White
    }
    val onPrimaryColor = when(type){
        ButtonType.PRIMARY -> Color.White
        ButtonType.SECONDARY -> color
        ButtonType.OUTLINE -> color
    }

    //OUTLINE BUTTON
    if (type == ButtonType.OUTLINE) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, primaryColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = primaryColor
            ),
            modifier = Modifier.width(width),
        ) {
            ButtonContent(
                icon = icon,
                text = text,
                primaryColor = primaryColor,
                onPrimaryColor = onPrimaryColor
            )
        }
    }
    //MAIN BUTTON
    else {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = onPrimaryColor
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.width(width)
        ) {
            ButtonContent(
                icon = icon,
                text = text,
                primaryColor = primaryColor,
                onPrimaryColor = onPrimaryColor
            )
        }
    }
}

@Composable
fun ButtonContent(
    icon: ImageVector? = null,
    text: String? = null,
    primaryColor: Color,
    onPrimaryColor:Color
){
    //ICON SECTION
    if(icon != null){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = onPrimaryColor
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    //TEXT SECTION
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text?: "",
        maxLines = 2,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
@Preview
fun ButtonPreview(){
    SimpleButton(
        onClick = {},
        icon = ImageVector.vectorResource(R.drawable.ic_plus),
        text = "New",
        color = Blue,
        type = ButtonType.PRIMARY
    )
}