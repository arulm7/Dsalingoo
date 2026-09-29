package com.app.dsalingo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.dsalingo.ui.MainViewModel
import com.app.dsalingo.ui.navigation.Screen
import com.app.dsalingo.ui.theme.*

@Composable
fun MainScaffold(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val user by mainViewModel.currentUser.collectAsState()
    val hearts by mainViewModel.hearts.collectAsState()
    val isFull by mainViewModel.isFull.collectAsState()
    var showHeartDialog by remember { mutableStateOf(false) }

    var showSideMenu by remember { mutableStateOf(false) }

    val isPrivateScreen = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.Learn.route,
        Screen.CategoryDetail.route,
        Screen.Challenges.route,
        Screen.Leaderboard.route,
        Screen.Profile.route
    )

    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.Learn.route,
        Screen.Challenges.route,
        Screen.Leaderboard.route,
        Screen.Profile.route
    )

    if (showHeartDialog) {
        HeartRechargeDialog(
            hearts = hearts,
            formattedTimeRemaining = mainViewModel.getFormattedTime(),
            rechargeProgress = mainViewModel.getRechargeProgress(),
            isFull = isFull,
            onRefillOneHeart = { mainViewModel.refillOneHeart() },
            onRefillAllHearts = { mainViewModel.refillAllHearts() },
            onDismiss = { showHeartDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(DuoDarkBg)) {
        Scaffold(
            containerColor = DuoDarkBg,
            topBar = {
                if (isPrivateScreen) {
                    DuolingoTopStatsHeader(
                        onMenuClick = { showSideMenu = true },
                        onHeartClick = { showHeartDialog = true },
                        streakCount = user?.streak ?: 0,
                        gemsCount = user?.xp ?: 0,
                        heartsCount = hearts
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    DuoFloatingBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DuoDarkBg)
            ) {
                content(paddingValues)
            }
        }

        // Slide-out Side Drawer Menu (Matching SwiftUI sideMenuDrawer)
        AnimatedVisibility(
            visible = showSideMenu,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.opacity(0.6f))
                    .clickable { showSideMenu = false }
            )
        }

        AnimatedVisibility(
            visible = showSideMenu,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            DuolingoSideDrawer(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    showSideMenu = false
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onClose = { showSideMenu = false }
            )
        }
    }
}

// Extension helper for opacity
private fun Color.opacity(alpha: Float): Color = this.copy(alpha = alpha)

/**
 * Top Stats Header (Matching SwiftUI topStatsHeader)
 */
@Composable
fun DuolingoTopStatsHeader(
    onMenuClick: () -> Unit,
    onHeartClick: () -> Unit = {},
    streakCount: Int = 12,
    gemsCount: Int = 500,
    heartsCount: Int = 5
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DuoDarkBg)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger Menu Button
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onMenuClick() },
                shape = CircleShape,
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // DSA / Language Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚡ DSA", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Streak 🔥
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("🔥", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "$streakCount",
                    fontWeight = FontWeight.Black,
                    color = DuoOrange,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Gems 💎
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("💎", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "$gemsCount",
                    fontWeight = FontWeight.Black,
                    color = DuoBlue,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Hearts ❤️ (Interactive with Dialog)
            Surface(
                modifier = Modifier.clickable { onHeartClick() },
                shape = RoundedCornerShape(10.dp),
                color = DuoCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("❤️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$heartsCount",
                        fontWeight = FontWeight.Black,
                        color = DuoRed,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Bottom Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DuoInputBorder.copy(alpha = 0.6f))
        )
    }
}

/**
 * Duolingo Side Navigation Drawer
 */
@Composable
fun DuolingoSideDrawer(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(DuoDarkBg)
            .border(
                width = 1.dp,
                color = DuoInputBorder,
                shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp)
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            // Brand Logo
            Text(
                text = "dsalingo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = DuoGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DuoInputBorder)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val drawerItems = listOf(
                DrawerItem("LEARN", Screen.Learn.route, "🏡"),
                DrawerItem("PRACTICE", Screen.Challenges.route, "🔤"),
                DrawerItem("LEADERBOARDS", Screen.Leaderboard.route, "🛡️"),
                DrawerItem("DASHBOARD", Screen.Dashboard.route, "🎁"),
                DrawerItem("PROFILE", Screen.Profile.route, "👤")
            )

            drawerItems.forEach { item ->
                val isSelected = currentRoute == item.route || (item.route == Screen.Learn.route && currentRoute == Screen.CategoryDetail.route)
                val bgColor = if (isSelected) DuoBlue.copy(alpha = 0.18f) else Color.Transparent
                val strokeColor = if (isSelected) DuoBlue.copy(alpha = 0.4f) else Color.Transparent
                val textColor = if (isSelected) DuoBlue else Color.White

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigate(item.route) },
                    shape = RoundedCornerShape(14.dp),
                    color = bgColor,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, strokeColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer info
            Text(
                text = "DSALINGO v1.0 • DUOLINGO UI",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DuoSubtext.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

data class DrawerItem(val title: String, val route: String, val emoji: String)
