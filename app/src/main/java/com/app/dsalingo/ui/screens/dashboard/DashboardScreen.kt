package com.app.dsalingo.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.components.*
import com.app.dsalingo.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToLearn: () -> Unit,
    onNavigateToChallenges: () -> Unit
) {
    val currentStreak = 12
    val currentXp = 35
    val targetXp = 50

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            CharacterGreetingSection(
                username = "Learner",
                streakDays = currentStreak,
                onContinueClick = onNavigateToLearn
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            DashboardStatsRow(streakDays = currentStreak)
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            DailyGoalCard(currentXp = currentXp, targetXp = targetXp)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            DuoButton(
                text = "CONTINUE LEARNING",
                onClick = onNavigateToLearn,
                color = DuoGreen,
                shadowColor = DuoGreenDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            DuoButton(
                text = "TAKE CHALLENGE",
                onClick = onNavigateToChallenges,
                color = DuoBlue,
                shadowColor = DuoBlueDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            AchievementsSection()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            ActivityCalendar()
        }
    }
}

/**
 * Contextual Mascot Area on the Dashboard with dynamic greeting and quick continue action.
 */
@Composable
fun CharacterGreetingSection(
    username: String,
    streakDays: Int,
    onContinueClick: () -> Unit
) {
    // Contextual message calculation
    val (greetingText, emotion) = remember(streakDays) {
        when {
            streakDays >= 10 -> "You're on fire! Keep your $streakDays-day streak alive!" to CharacterEmotion.EXCITED
            streakDays >= 3 -> "Welcome back, $username! Ready for today's challenge?" to CharacterEmotion.WELCOME
            else -> "One small lesson today keeps your DSA skills sharp!" to CharacterEmotion.IDLE
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DuoCharacter(
                emotion = emotion,
                size = 80.dp,
                showAura = true,
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.width(14.dp))

            CharacterSpeechBubble(
                message = greetingText,
                title = "WELCOME BACK!",
                actionText = "CONTINUE",
                onActionClick = onContinueClick,
                pointerDirection = BubblePointerDirection.LEFT,
                backgroundColor = Color(0xFF1B2830),
                borderColor = DuoInputBorder,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DashboardStatsRow(streakDays: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AnimatedStreakBadge(
            streakDays = streakDays,
            modifier = Modifier.weight(1f)
        )

        StatBadge(
            icon = "👑",
            value = "45",
            label = "Crowns",
            color = AmberGold,
            modifier = Modifier.weight(1f)
        )

        StatBadge(
            icon = "💎",
            value = "500",
            label = "Gems",
            color = DuoBlue,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatBadge(icon: String, value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = color)
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DuoSubtext)
        }
    }
}

@Composable
fun DailyGoalCard(currentXp: Int, targetXp: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY GOAL",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = DuoSubtext
                )
                Text(
                    text = "$currentXp / $targetXp XP",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = AmberGold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedXPProgressBar(
                currentXp = currentXp,
                targetXp = targetXp,
                barColor = DuoOrange
            )
        }
    }
}

@Composable
fun AchievementsSection() {
    Column {
        Text(
            text = "Achievements",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AchievementCircle("🥇", "Champion")
            AchievementCircle("🎯", "Sharpshooter")
            AchievementCircle("🚀", "Speed Demon")
            AchievementCircle("💎", "Gem Hoarder")
        }
    }
}

@Composable
fun AchievementCircle(icon: String, name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(icon, fontSize = 26.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DuoSubtext)
    }
}

@Composable
fun ActivityCalendar() {
    Column {
        Text(
            text = "Activity Streak",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(10) { col ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(7) { row ->
                                val isActive = (col + row) % 3 != 0
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(
                                            if (isActive) DuoGreen else DuoInputBg,
                                            RoundedCornerShape(3.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
