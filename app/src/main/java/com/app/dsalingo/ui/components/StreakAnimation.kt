package com.app.dsalingo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*

/**
 * Animated Streak Badge that pulses the flame and highlights milestone streaks.
 */
@Composable
fun AnimatedStreakBadge(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    val isMilestone = streakDays in listOf(7, 14, 30, 50, 100, 365)

    val infiniteTransition = rememberInfiniteTransition(label = "flamePulse")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isMilestone) 1.25f else 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isMilestone) 500 else 800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isMilestone) 2.dp else 1.5.dp,
            color = if (isMilestone) DuoOrange else DuoInputBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isMilestone) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .scale(flameScale)
                            .background(DuoOrange.copy(alpha = 0.35f), CircleShape)
                    )
                }
                Text(
                    text = "🔥",
                    fontSize = 22.sp,
                    modifier = Modifier.scale(flameScale)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$streakDays",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = DuoOrange
            )

            Text(
                text = if (isMilestone) "MILESTONE!" else "Streak",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMilestone) DuoOrange else DuoSubtext
            )
        }
    }
}
