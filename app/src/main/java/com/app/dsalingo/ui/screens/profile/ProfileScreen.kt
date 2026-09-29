package com.app.dsalingo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.theme.*

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            // 1. User Header & Avatar Card (Matching ProfileView.swift)
            DuolingoProfileHeaderCard(onSignOut = onSignOut)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // 2. Statistics Section (2x2 Grid)
            DuolingoProfileStatsGrid()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // 3. Achievements Section (Wildfire, Sage, Champion)
            DuolingoAchievementsSection()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // 4. Add Friends Section (Find Friends, Invite Friends)
            DuolingoAddFriendsSection()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // 5. Super Duolingo Upgrade Banner
            DuolingoSuperHeroBanner()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * User Header & Avatar Card (Matching ProfileView.swift userHeaderCard)
 */
@Composable
fun DuolingoProfileHeaderCard(onSignOut: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Avatar card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Settings & Sign Out Buttons (Top Right)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = DuoDarkBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder),
                        modifier = Modifier.size(36.dp).clickable { /* Edit profile */ }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DuoSubtext, modifier = Modifier.size(16.dp))
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = DuoDarkBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder),
                        modifier = Modifier.size(36.dp).clickable { onSignOut() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out", tint = DuoRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // Centered Dashed Silhouette Graphic
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(90.dp),
                        shape = CircleShape,
                        color = DuoDarkBg,
                        border = androidx.compose.foundation.BorderStroke(2.dp, DuoBlue)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = DuoBlue,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Alex Coder",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "@alexcoder",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoSubtext
                )
                Text(
                    text = "Joined September 2026",
                    fontSize = 12.sp,
                    color = DuoSubtext.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "12 Following",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoBlue
                    )
                    Text(
                        text = "48 Followers",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoBlue
                    )
                }
            }

            // Flag badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "⚡ DSA",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Statistics 2x2 Grid (Matching ProfileView.swift statisticsSection)
 */
@Composable
fun DuolingoProfileStatsGrid() {
    Column {
        Text(
            text = "Statistics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DuolingoStatCard(modifier = Modifier.weight(1f), icon = "🔥", value = "12", label = "Day Streak")
            DuolingoStatCard(modifier = Modifier.weight(1f), icon = "⚡", value = "5,200 XP", label = "Total XP")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DuolingoStatCard(modifier = Modifier.weight(1f), icon = "🛡️", value = "Bronze", label = "Current League")
            DuolingoStatCard(modifier = Modifier.weight(1f), icon = "🎯", value = "4", label = "Top 3 Finishes")
        }
    }
}

@Composable
fun DuolingoStatCard(modifier: Modifier = Modifier, icon: String, value: String, label: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoSubtext
                )
            }
        }
    }
}

/**
 * Achievements Section (Wildfire, Sage, Champion)
 */
@Composable
fun DuolingoAchievementsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Achievements",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "VIEW ALL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = DuoBlue
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        DuolingoAchievementRow(
            bgColor = DuoRed,
            icon = "🔥",
            level = 1,
            title = "Wildfire",
            subtitle = "Reach a 3 day streak",
            progress = 3,
            maxProgress = 3
        )
        Spacer(modifier = Modifier.height(10.dp))

        DuolingoAchievementRow(
            bgColor = DuoGreen,
            icon = "🧙‍♂️",
            level = 2,
            title = "Sage",
            subtitle = "Earn 500 XP in DSA lessons",
            progress = 350,
            maxProgress = 500
        )
        Spacer(modifier = Modifier.height(10.dp))

        DuolingoAchievementRow(
            bgColor = DuoPurple,
            icon = "🛡️",
            level = 1,
            title = "Champion",
            subtitle = "Advance to the Silver League",
            progress = 1,
            maxProgress = 1
        )
    }
}

@Composable
fun DuolingoAchievementRow(
    bgColor: Color,
    icon: String,
    level: Int,
    title: String,
    subtitle: String,
    progress: Int,
    maxProgress: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(width = 50.dp, height = 56.dp),
                shape = RoundedCornerShape(14.dp),
                color = bgColor
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(icon, fontSize = 20.sp)
                    Text(
                        text = "LVL $level",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
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
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "$progress/$maxProgress",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoSubtext
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DuoInputBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((progress.toFloat() / maxProgress.toFloat()).coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(bgColor)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = DuoSubtext
                )
            }
        }
    }
}

/**
 * Add Friends Section
 */
@Composable
fun DuolingoAddFriendsSection() {
    Column {
        Text(
            text = "Add Friends",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Find Friends
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { /* Find */ },
            shape = RoundedCornerShape(16.dp),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = DuoBlue.copy(alpha = 0.2f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔍", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Find Friends",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text("›", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DuoSubtext)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Invite Friends
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { /* Invite */ },
            shape = RoundedCornerShape(16.dp),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = DuoGreen.copy(alpha = 0.2f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📩", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Invite Friends",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text("›", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DuoSubtext)
            }
        }

    }
}

/**
 * Super Duolingo Hero Banner matching ShopView.swift
 */
@Composable
fun DuolingoSuperHeroBanner() {
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
                        listOf(Color(0xFF1E2E7B), Color(0xFF611E94))
                    ),
                    RoundedCornerShape(22.dp)
                )
                .border(
                    2.dp,
                    Brush.linearGradient(listOf(DuoBlue, DuoPurple, Color(0xFFFF62A5))),
                    RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🦉", fontSize = 38.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Super DSALingo",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Cyan
                    ) {
                        Text(
                            text = "SUPER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoDarkBg,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Fast-track your DSA mastery with unlimited hearts, custom practice tests, and zero interruptions.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                DuoButton(
                    text = "START FREE 7 DAYS",
                    onClick = {},
                    color = Color.White,
                    shadowColor = Color(0xFFD4D4D4),
                    textColor = DuoDarkBg,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
