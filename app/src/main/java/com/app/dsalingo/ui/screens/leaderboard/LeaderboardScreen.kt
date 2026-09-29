package com.app.dsalingo.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val xp: Int,
    val avatar: String,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val rankings = listOf(
        LeaderboardUser(1, "Vikram S.", 540, "🦊"),
        LeaderboardUser(2, "Sarah M.", 480, "🦉"),
        LeaderboardUser(3, "You (Learner)", 420, "👤", isCurrentUser = true),
        LeaderboardUser(4, "Alex K.", 360, "🦁"),
        LeaderboardUser(5, "Elena R.", 310, "🦄"),
        LeaderboardUser(6, "Chen W.", 280, "🐼"),
        LeaderboardUser(7, "Priya N.", 220, "🐯"),
        LeaderboardUser(8, "David L.", 180, "🐻"),
        LeaderboardUser(9, "Marcus B.", 150, "🐨"),
        LeaderboardUser(10, "Amina T.", 120, "🦊")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            // 1. Duolingo 3-Shield Graphic Header (Matching SwiftUI LeaderboardsView)
            Duolingo3ShieldHeader()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bronze League",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Top 20 advance to the Silver League",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoGreen
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            // "WHAT ARE LEADERBOARDS?" Card
            WhatAreLeaderboardsCard()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // League Timeframe Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoCardBg, RoundedCornerShape(16.dp))
                    .border(1.dp, DuoInputBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("This Week", "This Month", "All Time").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) DuoBlue else Color.Transparent
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else DuoSubtext,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Active Rankings List
        items(rankings) { item ->
            DuolingoLeaderboardRow(item)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * 3-Shield Graphic Header (Matching lockedHeaderGraphic from LeaderboardsView.swift)
 */
@Composable
fun Duolingo3ShieldHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Sparkle Stars
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("✨", fontSize = 20.sp)
            Text("🌟", fontSize = 18.sp)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            // Left Bronze Shield
            Surface(
                modifier = Modifier
                    .size(width = 65.dp, height = 75.dp)
                    .rotate(-14f)
                    .offset(y = 8.dp, x = 10.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF9E5A2A),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🛡️", fontSize = 28.sp)
                }
            }

            // Center Gold Shield
            Surface(
                modifier = Modifier
                    .size(width = 85.dp, height = 95.dp),
                shape = RoundedCornerShape(22.dp),
                color = AmberGold,
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.6f)),
                shadowElevation = 10.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🔥", fontSize = 40.sp)
                }
            }

            // Right Silver Shield
            Surface(
                modifier = Modifier
                    .size(width = 65.dp, height = 75.dp)
                    .rotate(14f)
                    .offset(y = 8.dp, x = (-10).dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFA8B2B8),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🛡️", fontSize = 28.sp)
                }
            }
        }
    }
}


@Composable
fun WhatAreLeaderboardsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "WHAT ARE LEADERBOARDS?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoSubtext
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Do lessons, earn XP!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Earn XP to compete with learners and unlock higher leagues.",
                    fontSize = 12.sp,
                    color = DuoSubtext,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = CircleShape,
                color = DuoGreen.copy(alpha = 0.2f),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🦉", fontSize = 32.sp)
                }
            }
        }
    }
}

@Composable
fun DuolingoLeaderboardRow(item: LeaderboardUser) {
    val bgColor = if (item.isCurrentUser) DuoGreen.copy(alpha = 0.12f) else DuoCardBg
    val borderColor = if (item.isCurrentUser) DuoGreen else DuoInputBorder

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(if (item.isCurrentUser) 2.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = "${item.rank}",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = if (item.rank <= 3) AmberGold else DuoSubtext,
                modifier = Modifier.width(26.dp)
            )

            // Avatar
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = if (item.isCurrentUser) DuoGreen.copy(alpha = 0.3f) else DuoInputBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.avatar, fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name
            Text(
                text = item.name,
                fontWeight = if (item.isCurrentUser) FontWeight.Black else FontWeight.Bold,
                fontSize = 15.sp,
                color = if (item.isCurrentUser) DuoGreen else Color.White,
                modifier = Modifier.weight(1f)
            )

            // XP Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DuoInputBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
            ) {
                Text(
                    text = "${item.xp} XP",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = DuoSubtext,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}
