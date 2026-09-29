package com.app.dsalingo.ui.screens.challenges

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.components.DuoTextField
import com.app.dsalingo.ui.theme.*

data class DsaChallenge(
    val id: Int,
    val title: String,
    val topic: String,
    val difficulty: String,
    val xp: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var earnedXp by remember { mutableIntStateOf(10) }

    val challenges = listOf(
        DsaChallenge(1, "Two Sum Problem", "Array", "Easy", 50),
        DsaChallenge(2, "Valid Palindrome", "String", "Easy", 50),
        DsaChallenge(3, "Reverse Linked List", "Linked List", "Easy", 50),
        DsaChallenge(4, "Longest Substring Without Repeating", "String", "Medium", 75),
        DsaChallenge(5, "3Sum Zero Triplet", "Array", "Medium", 75),
        DsaChallenge(6, "Container With Most Water", "Array", "Medium", 75),
        DsaChallenge(7, "LRU Cache Design", "Linked List", "Hard", 100),
        DsaChallenge(8, "Trapping Rain Water", "Array", "Hard", 100),
        DsaChallenge(9, "Binary Tree Level Order Traversal", "Tree", "Medium", 75),
        DsaChallenge(10, "Merge k Sorted Lists", "Heap", "Hard", 100)
    )

    val filteredChallenges = challenges.filter {
        (selectedFilter == "All" || it.topic.equals(selectedFilter, ignoreCase = true)) &&
        (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.topic.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            // 1. Purple Hero Welcome Banner (Matching QuestsView.swift)
            QuestsWelcomeHeroBanner()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // 2. Daily Quests Section Header with Timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Daily Quests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DuoOrange.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DuoOrange.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "⏰ 9 HOURS LEFT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoOrange,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            // Quest 1 Card: Earn 10 XP
            QuestCard(
                title = "Earn 10 XP in DSA Lessons",
                current = minOf(earnedXp, 10),
                total = 10,
                icon = "⚡",
                isCompleted = earnedXp >= 10,
                color = DuoYellow
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Quest 2 Card: Spend 15 minutes learning
            QuestCard(
                title = "Spend 15 mins practicing problems",
                current = 8,
                total = 15,
                icon = "🔥",
                isCompleted = false,
                color = DuoOrange
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // DSA Code Challenges Header
            Text(
                text = "Code Challenges",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Curated problems from Striver's A2Z DSA Sheet",
                fontSize = 13.sp,
                color = DuoSubtext
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Stats row (Total, Easy, Medium, Hard)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChallengeStatBox(modifier = Modifier.weight(1f), label = "Total", value = "105", color = Color.White)
                ChallengeStatBox(modifier = Modifier.weight(1f), label = "Easy", value = "42", color = DuoGreen)
                ChallengeStatBox(modifier = Modifier.weight(1f), label = "Medium", value = "48", color = AmberGold)
                ChallengeStatBox(modifier = Modifier.weight(1f), label = "Hard", value = "15", color = DuoRed)
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Search Box
            DuoTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search challenges..."
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("All", "Array", "String", "Linked List", "Tree", "Heap")
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        modifier = Modifier.clickable { selectedFilter = filter },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) DuoBlue else DuoCardBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) DuoBlue else DuoInputBorder)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else DuoSubtext,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Filtered Challenges list
        items(filteredChallenges) { challenge ->
            ChallengeItemCard(challenge)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun QuestsWelcomeHeroBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF8554E0), Color(0xFF6B38C7))
                    ),
                    RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WELCOME TO QUESTS!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Complete daily challenges to earn gems and unlock secret reward chests!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎁", fontSize = 38.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestCard(
    title: String,
    current: Int,
    total: Int,
    icon: String,
    isCompleted: Boolean,
    color: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(
            if (isCompleted) 2.dp else 1.dp,
            if (isCompleted) DuoGreen else DuoInputBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.18f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    if (isCompleted) {
                        Text(
                            text = "✓ DONE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoGreen
                        )
                    } else {
                        Text(
                            text = "$current / $total",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoSubtext
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(DuoInputBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((current.toFloat() / total.toFloat()).coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (isCompleted) DuoGreen else color)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(if (isCompleted) "🎁" else "📦", fontSize = 22.sp)
        }
    }
}

@Composable
fun ChallengeStatBox(modifier: Modifier = Modifier, label: String, value: String, color: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = color)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DuoSubtext)
        }
    }
}

@Composable
fun ChallengeItemCard(challenge: DsaChallenge) {
    val difficultyColor = when(challenge.difficulty) {
        "Easy" -> DuoGreen
        "Medium" -> AmberGold
        else -> DuoRed
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(difficultyColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = DuoInputBg,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
                    ) {
                        Text(
                            text = challenge.topic,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoSubtext,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+${challenge.xp} XP",
                        fontWeight = FontWeight.Black,
                        color = AmberGold,
                        fontSize = 12.sp
                    )
                }
            }

            DuoButton(
                text = "START",
                onClick = {},
                height = 36.dp,
                cornerRadius = 10.dp,
                fontSize = 12,
                color = DuoBlue,
                shadowColor = DuoBlueDark,
                modifier = Modifier.width(80.dp)
            )
        }
    }
}
