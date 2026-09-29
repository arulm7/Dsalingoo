package com.app.dsalingo.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.components.DuoSecondaryButton
import com.app.dsalingo.ui.components.DuoBackpackMascot
import com.app.dsalingo.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToLearn: () -> Unit,
    onNavigateToChallenges: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            WelcomeBanner(username = "Learner")
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            StatsRow()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            DailyGoalCard(currentXp = 35, targetXp = 50)
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

@Composable
fun WelcomeBanner(username: String) {
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
            DuoBackpackMascot(
                modifier = Modifier.size(72.dp),
                speechText = "WELCOME BACK!"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Welcome back, $username!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Ready to crush some DSA problems?",
                    color = DuoSubtext,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatBadge(icon = "🔥", value = "12", label = "Streak", color = DuoOrange, modifier = Modifier.weight(1f))
        StatBadge(icon = "👑", value = "45", label = "Crowns", color = AmberGold, modifier = Modifier.weight(1f))
        StatBadge(icon = "💎", value = "500", label = "Gems", color = DuoBlue, modifier = Modifier.weight(1f))
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
                    text = "$currentXp/$targetXp XP",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = AmberGold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DuoInputBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth((currentXp.toFloat() / targetXp.toFloat()).coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(6.dp))
                        .background(DuoOrange)
                )
            }
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
