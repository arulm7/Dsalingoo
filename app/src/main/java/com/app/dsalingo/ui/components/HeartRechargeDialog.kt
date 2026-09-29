package com.app.dsalingo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.dsalingo.ui.theme.*

@Composable
fun HeartRechargeDialog(
    hearts: Int,
    maxHearts: Int = 5,
    formattedTimeRemaining: String,
    rechargeProgress: Float,
    isFull: Boolean,
    onRefillOneHeart: () -> Unit,
    onRefillAllHearts: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseHeart")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(2.dp, DuoInputBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HEARTS RECHARGE",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = DuoSubtext,
                        letterSpacing = 1.sp
                    )

                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDismiss() },
                        shape = CircleShape,
                        color = DuoInputBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = DuoSubtext,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animated Heart Display Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..maxHearts) {
                        val isFilled = i <= hearts
                        val isCharging = i == hearts + 1 && !isFull
                        
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) DuoRed.copy(alpha = 0.15f) else DuoInputBg)
                                .border(
                                    width = if (isCharging) 2.dp else 1.dp,
                                    color = if (isFilled) DuoRed else if (isCharging) DuoOrange else DuoInputBorder,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFilled) {
                                Text(
                                    text = "❤️",
                                    fontSize = 20.sp,
                                    modifier = if (isFull) Modifier.scale(pulseScale) else Modifier
                                )
                            } else if (isCharging) {
                                Text(
                                    text = "⚡",
                                    fontSize = 18.sp,
                                    modifier = Modifier.scale(pulseScale)
                                )
                            } else {
                                Text(
                                    text = "🖤",
                                    fontSize = 18.sp,
                                    color = DuoSubtext.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (isFull) {
                    Text(
                        text = "Full Health! 💖",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You have all 5 hearts ready for your next DSA lessons.",
                        fontSize = 13.sp,
                        color = DuoSubtext,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Next Heart in $formattedTimeRemaining",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoOrange
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { rechargeProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = DuoOrange,
                        trackColor = DuoInputBg
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+1 Heart charges every 5 minutes",
                        fontSize = 12.sp,
                        color = DuoSubtext,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!isFull) {
                        DuoButton(
                            text = "PRACTICE FOR +1 HEART ❤️",
                            onClick = {
                                onRefillOneHeart()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = DuoGreen,
                            shadowColor = DuoGreenDark
                        )
                    }

                    DuoButton(
                        text = if (isFull) "KEEP LEARNING" else "REFILL TO FULL (5 ❤️)",
                        onClick = {
                            if (!isFull) onRefillAllHearts()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = DuoBlue,
                        shadowColor = DuoBlueDark
                    )
                }
            }
        }
    }
}
