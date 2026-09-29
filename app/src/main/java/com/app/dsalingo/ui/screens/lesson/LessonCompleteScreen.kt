package com.app.dsalingo.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.components.*
import com.app.dsalingo.ui.theme.*

@Composable
fun LessonCompleteScreen(
    xpReward: Int,
    accuracyPercent: Int = 100,
    onContinue: () -> Unit
) {
    val isPerfect = accuracyPercent == 100

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
    ) {
        // Full screen celebratory confetti overlay
        FullScreenCelebrationOverlay(isVisible = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Animated Celebrating Mascot
                DuoCharacter(
                    emotion = if (isPerfect) CharacterEmotion.PERFECT else CharacterEmotion.LESSON_COMPLETE,
                    size = 110.dp,
                    showAura = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                CharacterSpeechBubble(
                    message = if (isPerfect) "PERFECT SCORE! You didn't make a single mistake!" else "Great job! Another DSA lesson conquered!",
                    title = if (isPerfect) "PERFECT RUN 🏆" else "LESSON COMPLETE",
                    backgroundColor = if (isPerfect) AmberGold.copy(alpha = 0.25f) else DuoGreen.copy(alpha = 0.25f),
                    borderColor = if (isPerfect) AmberGold else DuoGreen,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isPerfect) "PERFECT!" else "Lesson Complete!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isPerfect) AmberGold else DuoGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "You're building unstoppable DSA mastery!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoSubtext,
                    textAlign = TextAlign.Center
                )
            }

            // Stats Cards (Total XP, Accuracy %)
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
                            text = "TOTAL XP EARNED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoSubtext
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AnimatedXPCounter(
                            targetXp = xpReward,
                            fontSize = 26,
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
                            color = if (isPerfect) AmberGold else DuoGreen
                        )
                    }
                }
            }

            DuoButton(
                text = "CONTINUE",
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = DuoGreen,
                shadowColor = DuoGreenDark
            )
        }
    }
}
