package com.app.dsalingo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.navigation.Screen
import com.app.dsalingo.ui.theme.*

@Composable
fun DuoFloatingBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth()
            .height(68.dp),
        shape = RoundedCornerShape(24.dp),
        color = DuoCardBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                BottomNavItem("Learn", Screen.Learn.route, Icons.Default.Home, "🏡"),
                BottomNavItem("Practice", Screen.Challenges.route, Icons.AutoMirrored.Filled.List, "🔤"),
                BottomNavItem("Leagues", Screen.Leaderboard.route, Icons.Default.Star, "🛡️"),
                BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person, "👤")
            )

            items.forEach { item ->
                val isSelected = currentRoute == item.route || (item.route == Screen.Learn.route && currentRoute == Screen.CategoryDetail.route)
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(if (isPressed) 0.88f else 1f, label = "scale")

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (!isSelected) {
                                onNavigate(item.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val tint = if (isSelected) DuoBlue else DuoSubtext
                        val bgColor = if (isSelected) DuoBlue.copy(alpha = 0.18f) else Color.Transparent
                        val strokeColor = if (isSelected) DuoBlue.copy(alpha = 0.4f) else Color.Transparent

                        Box(
                            modifier = Modifier
                                .size(48.dp, 34.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .then(
                                    if (isSelected) Modifier.background(bgColor, RoundedCornerShape(12.dp))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = tint,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (isSelected) {
                            Text(
                                text = item.label.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = DuoBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector,
    val emoji: String = "⭐"
)
