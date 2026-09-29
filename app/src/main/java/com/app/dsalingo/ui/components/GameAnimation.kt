package com.app.dsalingo.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Game Animation Events that can be triggered across screens.
 */
sealed class GameAnimationEvent {
    data class Correct(val xpGained: Int = 10) : GameAnimationEvent()
    data class Wrong(val reason: String = "Keep going!") : GameAnimationEvent()
    data class LessonComplete(val totalXp: Int, val isPerfect: Boolean = false) : GameAnimationEvent()
    data object Perfect : GameAnimationEvent()
    data object HeartLost : GameAnimationEvent()
    data class LevelUp(val newLevel: Int) : GameAnimationEvent()
    data object BossDefeated : GameAnimationEvent()
    data class StreakMilestone(val days: Int) : GameAnimationEvent()
}

/**
 * Overlay component that can display full-screen confetti or victory bursts on major events.
 */
@Composable
fun FullScreenCelebrationOverlay(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(500)),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimationRawRes(
                resId = R.raw.confetti,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
