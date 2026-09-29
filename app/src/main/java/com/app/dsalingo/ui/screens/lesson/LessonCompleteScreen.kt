package com.app.dsalingo.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.components.LottieAnimationRawRes
import com.app.dsalingo.ui.theme.*

@Composable
fun LessonCompleteScreen(
    xpReward: Int,
    accuracyPercent: Int = 100,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", fontSize = 70.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lesson Complete!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = DuoGreen
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You're making great progress mastering DSA!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DuoSubtext
            )
        }

        // 2 Stats Cards (Total XP, Accuracy %)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoSubtext
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+$xpReward",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = AmberGold
                    )
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ACCURACY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoSubtext
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$accuracyPercent%",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoGreen
                    )
                }
            }
        }

        DuoButton(
            text = "CONTINUE",
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            color = DuoGreen,
            shadowColor = DuoGreenDark
        )
    }
}
