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
import com.app.dsalingo.ui.components.CharacterEmotion
import com.app.dsalingo.ui.components.CharacterSpeechBubble
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.components.DuoCharacter
import com.app.dsalingo.ui.theme.*

@Composable
fun GameOverScreen(
    formattedTimeRemaining: String = "05:00",
    rechargeProgress: Float = 0f,
    onPracticeRefill: () -> Unit = {},
    onQuit: () -> Unit
) {
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
            // Empathetic Mascot Reaction
            DuoCharacter(
                emotion = CharacterEmotion.HEART_LOST,
                size = 100.dp,
                showAura = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            CharacterSpeechBubble(
                message = "Don't worry! Everyone makes mistakes when learning DSA. You've got this!",
                title = "KEEP YOUR HEAD UP",
                backgroundColor = DuoCardBg,
                borderColor = DuoInputBorder,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Out of Hearts!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = DuoRed
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = "Hearts automatically recharge over time, or you can practice to earn them back.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DuoSubtext,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recharge Countdown Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formattedTimeRemaining,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
