package com.app.dsalingo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Reusable Dsalingoo Character Component
 *
 * Renders the mascot with rich emotional animations, dynamic glowing aura,
 * reaction micro-interactions, and optional Lottie celebration overlays.
 */
@Composable
fun DuoCharacter(
    emotion: CharacterEmotion = CharacterEmotion.IDLE,
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    showAura: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Emotion-specific infinite transitions
    val infiniteTransition = rememberInfiniteTransition(label = "mascotEmotionMotion")

    // Bobbing Y-offset
    val yOffset by infiniteTransition.animateFloat(
        initialValue = when (emotion) {
            CharacterEmotion.IDLE -> 0f
            CharacterEmotion.HAPPY -> -2f
            CharacterEmotion.EXCITED -> -6f
            CharacterEmotion.WELCOME -> -2f
            CharacterEmotion.LESSON_START -> -4f
            CharacterEmotion.THINKING -> -1f
            CharacterEmotion.CORRECT -> -14f
            CharacterEmotion.LESSON_COMPLETE, CharacterEmotion.PERFECT -> -18f
            CharacterEmotion.BOSS -> -6f
            CharacterEmotion.BOSS_COMPLETE -> -20f
            CharacterEmotion.HEART_LOST, CharacterEmotion.WRONG -> 2f
            CharacterEmotion.ENCOURAGING -> -2f
            CharacterEmotion.LEVEL_UP -> -16f
        },
        targetValue = when (emotion) {
            CharacterEmotion.IDLE -> -8f
            CharacterEmotion.HAPPY -> -10f
            CharacterEmotion.EXCITED -> -16f
            CharacterEmotion.WELCOME -> -12f
            CharacterEmotion.LESSON_START -> -14f
            CharacterEmotion.THINKING -> -5f
            CharacterEmotion.CORRECT -> 0f
            CharacterEmotion.LESSON_COMPLETE, CharacterEmotion.PERFECT -> 0f
            CharacterEmotion.BOSS -> 4f
            CharacterEmotion.BOSS_COMPLETE -> 0f
            CharacterEmotion.HEART_LOST, CharacterEmotion.WRONG -> -2f
            CharacterEmotion.ENCOURAGING -> -8f
            CharacterEmotion.LEVEL_UP -> 0f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (emotion) {
                    CharacterEmotion.CORRECT, CharacterEmotion.LESSON_COMPLETE, CharacterEmotion.PERFECT, CharacterEmotion.BOSS_COMPLETE -> 380
                    CharacterEmotion.EXCITED, CharacterEmotion.LESSON_START, CharacterEmotion.WELCOME -> 500
                    CharacterEmotion.BOSS -> 600
                    CharacterEmotion.THINKING -> 1200
                    CharacterEmotion.HEART_LOST, CharacterEmotion.WRONG -> 900
                    else -> 850
                },
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    // Rotation / tilt angle
    val tiltAngle by infiniteTransition.animateFloat(
        initialValue = when (emotion) {
            CharacterEmotion.THINKING -> -7f
            CharacterEmotion.WELCOME, CharacterEmotion.HAPPY -> -5f
            CharacterEmotion.EXCITED -> -6f
            CharacterEmotion.CORRECT, CharacterEmotion.LESSON_COMPLETE -> -4f
            CharacterEmotion.HEART_LOST -> 2f
            CharacterEmotion.BOSS -> -2f
            else -> 0f
        },
        targetValue = when (emotion) {
            CharacterEmotion.THINKING -> 7f
            CharacterEmotion.WELCOME, CharacterEmotion.HAPPY -> 5f
            CharacterEmotion.EXCITED -> 6f
            CharacterEmotion.CORRECT, CharacterEmotion.LESSON_COMPLETE -> 4f
            CharacterEmotion.HEART_LOST -> -2f
            CharacterEmotion.BOSS -> 2f
            else -> 0f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (emotion) {
                    CharacterEmotion.THINKING -> 1000
                    CharacterEmotion.EXCITED, CharacterEmotion.CORRECT, CharacterEmotion.LESSON_COMPLETE -> 300
                    else -> 700
                },
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tiltAngle"
    )

    // Aura pulse
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = if (emotion == CharacterEmotion.BOSS || emotion == CharacterEmotion.PERFECT || emotion == CharacterEmotion.EXCITED) 1.25f else 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )

    // Aura color
    val auraColor = when (emotion) {
        CharacterEmotion.CORRECT -> DuoGreen.copy(alpha = 0.45f)
        CharacterEmotion.PERFECT, CharacterEmotion.LEVEL_UP -> AmberGold.copy(alpha = 0.5f)
        CharacterEmotion.BOSS -> DuoRed.copy(alpha = 0.5f)
        CharacterEmotion.BOSS_COMPLETE -> DuoPurple.copy(alpha = 0.5f)
        CharacterEmotion.WRONG, CharacterEmotion.HEART_LOST -> DuoRed.copy(alpha = 0.3f)
        CharacterEmotion.THINKING -> DuoBlue.copy(alpha = 0.35f)
        CharacterEmotion.EXCITED -> DuoOrange.copy(alpha = 0.45f)
        else -> DuoGreen.copy(alpha = 0.3f)
    }

    // Reaction badge / icon
    val reactionIcon = when (emotion) {
        CharacterEmotion.CORRECT -> "✨"
        CharacterEmotion.PERFECT -> "🏆"
        CharacterEmotion.THINKING -> "💡"
        CharacterEmotion.WELCOME, CharacterEmotion.HAPPY -> "👋"
        CharacterEmotion.LESSON_START -> "🎯"
        CharacterEmotion.ENCOURAGING -> "💪"
        CharacterEmotion.HEART_LOST -> "💔"
        CharacterEmotion.BOSS -> "🔥"
        CharacterEmotion.BOSS_COMPLETE -> "👑"
        CharacterEmotion.LEVEL_UP -> "⭐"
        CharacterEmotion.EXCITED -> "⚡"
        else -> null
    }

    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Aura at base
        if (showAura) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-4).dp)
                    .size(width = size * 0.85f, height = size * 0.22f)
                    .scale(auraScale)
                    .background(auraColor, CircleShape)
            )
        }

        // Mascot Base Graphic with Animations
        Box(
            modifier = Modifier
                .size(size * 0.9f)
                .offset(y = yOffset.dp)
                .graphicsLayer {
                    rotationZ = tiltAngle
                    cameraDistance = 12f * density
                },
            contentAlignment = Alignment.Center
        ) {
            // Pick appropriate mascot asset
            val imageRes = if (emotion == CharacterEmotion.LESSON_START || emotion == CharacterEmotion.THINKING) {
                R.drawable.duo_pencil
            } else {
                R.drawable.duo_backpack
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Dsalingoo Owl Mascot",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            // Lottie celebration overlay for high-tier emotions
            if (emotion == CharacterEmotion.LESSON_COMPLETE || emotion == CharacterEmotion.BOSS_COMPLETE || emotion == CharacterEmotion.LEVEL_UP) {
                LottieAnimationRawRes(
                    resId = R.raw.duo_celebrate,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (emotion == CharacterEmotion.HEART_LOST) {
                LottieAnimationRawRes(
                    resId = R.raw.crying,
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }

        // Floating reaction accessory badge
        if (reactionIcon != null) {
            Text(
                text = reactionIcon,
                fontSize = (size.value * 0.25f).sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-6).dp)
            )
        }
    }
}
