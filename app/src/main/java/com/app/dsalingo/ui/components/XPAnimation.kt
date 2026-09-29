package com.app.dsalingo.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Animated XP Counter that smoothly interpolates to the target XP value
 */
@Composable
fun AnimatedXPCounter(
    targetXp: Int,
    modifier: Modifier = Modifier,
    fontSize: Int = 24,
    color: Color = AmberGold
) {
    val animatedXp by animateIntAsState(
        targetValue = targetXp,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "animatedXp"
    )

    Text(
        text = "$animatedXp XP",
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Black,
        color = color,
        modifier = modifier
    )
}

/**
 * Floating +XP badge that pops up, ascends, and fades out
 */
@Composable
fun FloatingXPGain(
    xpAmount: Int,
    trigger: Boolean,
    modifier: Modifier = Modifier,
    onFinished: (() -> Unit)? = null
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(trigger) {
        if (trigger) {
            isVisible = true
            delay(1400)
            isVisible = false
            onFinished?.invoke()
        }
    }

    val floatAnim = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            floatAnim.snapTo(0f)
            floatAnim.animateTo(
                targetValue = -35f,
                animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
            )
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(200)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
        exit = fadeOut(tween(400)),
        modifier = modifier.offset(y = floatAnim.value.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AmberGold,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+$xpAmount XP",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = DuoDarkBg
                )
            }
        }
    }
}

/**
 * Animated XP Progress Bar for daily goals and milestones
 */
@Composable
fun AnimatedXPProgressBar(
    currentXp: Int,
    targetXp: Int,
    modifier: Modifier = Modifier,
    barColor: Color = AmberGold
) {
    val progressRatio = if (targetXp > 0) (currentXp.toFloat() / targetXp.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressRatio,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "xpProgress"
    )

    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = barColor,
            trackColor = DuoInputBg
        )
    }
}
