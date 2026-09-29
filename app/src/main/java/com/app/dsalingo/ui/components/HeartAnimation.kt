package com.app.dsalingo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Animated Hearts Row for quiz topbar and profile with shake-on-loss and pulse effects.
 */
@Composable
fun AnimatedHeartsRow(
    hearts: Int,
    maxHearts: Int = 5,
    modifier: Modifier = Modifier,
    heartSize: Dp = 22.dp,
    onHeartClick: (() -> Unit)? = null
) {
    var previousHearts by remember { mutableIntStateOf(hearts) }
    var shakenHeartIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(hearts) {
        if (hearts < previousHearts) {
            // A heart was lost! Shake the heart that was just depleted
            shakenHeartIndex = hearts // e.g. 5 -> 4 means index 4 was lost
            delay(800)
            shakenHeartIndex = null
        }
        previousHearts = hearts
    }

    Row(
        modifier = modifier.clickable(enabled = onHeartClick != null) { onHeartClick?.invoke() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until maxHearts) {
            val isFilled = i < hearts
            val isShaking = shakenHeartIndex == i

            AnimatedSingleHeart(
                isFilled = isFilled,
                isShaking = isShaking,
                heartSize = heartSize
            )
        }
    }
}

@Composable
private fun AnimatedSingleHeart(
    isFilled: Boolean,
    isShaking: Boolean,
    heartSize: Dp
) {
    val shakeAnim = remember { Animatable(0f) }

    LaunchedEffect(isShaking) {
        if (isShaking) {
            // Rapid left-right shake
            repeat(3) {
                shakeAnim.animateTo(6f, tween(50, easing = LinearEasing))
                shakeAnim.animateTo(-6f, tween(50, easing = LinearEasing))
            }
            shakeAnim.animateTo(0f, tween(50))
        }
    }

    val scaleAnim by animateFloatAsState(
        targetValue = if (isFilled) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heartScale"
    )

    Box(
        modifier = Modifier
            .size(heartSize)
            .graphicsLayer {
                translationX = shakeAnim.value
            }
            .scale(scaleAnim),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isFilled) "❤️" else if (isShaking) "💔" else "🖤",
            fontSize = (heartSize.value * 0.85f).sp
        )
    }
}
