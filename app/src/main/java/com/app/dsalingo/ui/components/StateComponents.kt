package com.app.dsalingo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*

/**
 * Standard UI State pattern for asynchronous data operations
 */
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String, val canRetry: Boolean = true) : UiState<Nothing>
}

/**
 * Custom Shimmer Effect Modifier tailored for Dsalingoo's sleek dark theme
 */
fun Modifier.shimmerEffect(
    shape: Shape = RoundedCornerShape(16.dp),
    baseColor: Color = Color(0xFF18252D),
    highlightColor: Color = Color(0xFF2B3D47)
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 300f, translateAnim + 300f)
    )

    this
        .clip(shape)
        .background(brush)
}

/**
 * Shimmer placeholder box
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = modifier.shimmerEffect(shape = shape)
    )
}

/**
 * Shimmer Loading placeholder for Learn screen & Category learning paths
 */
@Composable
fun ShimmerLearningPath(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Banner placeholder
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Lesson nodes path skeleton
        repeat(4) { index ->
            val xOffset = when (index % 3) {
                0 -> 0.dp
                1 -> (-40).dp
                else -> 40.dp
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(x = xOffset)
                    .padding(vertical = 12.dp)
            ) {
                // Speech bubble preview skeleton
                if (index == 0) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(160.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Node circular button skeleton
                ShimmerBox(
                    modifier = Modifier.size(76.dp),
                    shape = CircleShape
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Title label skeleton
                ShimmerBox(
                    modifier = Modifier
                        .width(90.dp)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }
    }
}

/**
 * Shimmer Loading placeholder for Question Screen
 */
@Composable
fun ShimmerQuestionBody(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(10.dp))
            // Title skeleton
            ShimmerBox(modifier = Modifier.width(180.dp).height(24.dp))

            Spacer(modifier = Modifier.height(20.dp))

            // Character + bubble prompt skeleton
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerBox(modifier = Modifier.size(64.dp), shape = CircleShape)
                Spacer(modifier = Modifier.width(12.dp))
                ShimmerBox(modifier = Modifier.weight(1f).height(64.dp), shape = RoundedCornerShape(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Options skeleton
            repeat(4) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Button skeleton
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Friendly Error State Component featuring DuoCharacter and retry action
 */
@Composable
fun ErrorStateView(
    message: String = "Could not connect to Dsalingoo servers. Please check your internet connection.",
    modifier: Modifier = Modifier,
    title: String = "CONNECTION ERROR",
    canRetry: Boolean = true,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DuoCharacter(
            emotion = CharacterEmotion.ENCOURAGING,
            size = 100.dp,
            showAura = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CharacterSpeechBubble(
            message = message,
            title = title,
            backgroundColor = DuoCardBg,
            borderColor = DuoRed,
            modifier = Modifier.fillMaxWidth(0.92f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (canRetry) {
            DuoButton(
                text = "TRY AGAIN",
                onClick = onRetry,
                color = DuoGreen,
                shadowColor = DuoGreenDark,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(48.dp)
            )
        }
    }
}

/**
 * Empty State View for empty lists or unlocked features
 */
@Composable
fun EmptyStateView(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DuoCharacter(
            emotion = CharacterEmotion.THINKING,
            size = 80.dp,
            showAura = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = message,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = DuoSubtext,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        if (!actionText.isNullOrBlank() && onAction != null) {
            Spacer(modifier = Modifier.height(20.dp))
            DuoButton(
                text = actionText,
                onClick = onAction,
                color = DuoGreen,
                shadowColor = DuoGreenDark,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}
