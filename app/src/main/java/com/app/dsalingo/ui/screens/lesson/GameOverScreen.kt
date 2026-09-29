package com.app.dsalingo.ui.screens.lesson

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.theme.*

@Composable
fun GameOverScreen(
    formattedTimeRemaining: String = "05:00",
    rechargeProgress: Float = 0f,
    onPracticeRefill: () -> Unit = {},
    onQuit: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseHeart")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "💔",
                fontSize = 72.sp,
                modifier = Modifier.scale(heartScale)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Out of Hearts!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = DuoRed
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "You need at least 1 heart to practice lessons. Hearts automatically recharge over time.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = DuoSubtext,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Recharge Countdown Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NEXT HEART CHARGES IN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoOrange,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = formattedTimeRemaining,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { rechargeProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = DuoOrange,
                        trackColor = DuoInputBg,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "+1 ❤️ every 5 minutes",
                        fontSize = 11.sp,
                        color = DuoSubtext,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DuoButton(
                text = "PRACTICE FOR +1 HEART ❤️",
                onClick = onPracticeRefill,
                modifier = Modifier.fillMaxWidth(),
                color = DuoGreen,
                shadowColor = DuoGreenDark
            )

            DuoButton(
                text = "BACK TO DASHBOARD",
                onClick = onQuit,
                modifier = Modifier.fillMaxWidth(),
                color = DuoCardBg,
                shadowColor = DuoInputBorder,
                textColor = DuoSubtext
            )
        }
    }
}

