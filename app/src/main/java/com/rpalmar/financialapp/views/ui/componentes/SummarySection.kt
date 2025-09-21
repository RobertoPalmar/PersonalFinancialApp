package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.White

@Composable
fun SummarySection(
    sectionName:String,
    totalEntities:Int,
    mainSummaryData:String? = null,
    mainColor:Color,
    icon:ImageVector
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(mainColor),
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .clipToBounds()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = sectionName,
                tint = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(140.dp)
                    .offset(x = 15.dp, y = 0.dp)
            )
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(1f)
            ) {
                Text(
                    text = sectionName,
                    color = White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total ${sectionName}: ${totalEntities}",
                    color = White,
                    style = MaterialTheme.typography.bodyMedium,
                )

                if(mainSummaryData != null){
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = mainSummaryData,
                        color = White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight =  FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SectionPreview(){
    SummarySection(
        sectionName = "Accounts",
        totalEntities = 15,
        mainSummaryData = "Balance: 1850.15 $",
        mainColor = Blue,
        icon = ImageVector.vectorResource(id = R.drawable.ic_wallet)
    )
}