package com.app.dsalingo.ui.screens.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.components.BubblePointerDirection
import com.app.dsalingo.ui.components.CharacterEmotion
import com.app.dsalingo.ui.components.CharacterSpeechBubble
import com.app.dsalingo.ui.components.DuoButton
import com.app.dsalingo.ui.components.DuoSecondaryButton
import com.app.dsalingo.ui.components.DuoBackpackMascot
import com.app.dsalingo.ui.components.DuoCharacter
import com.app.dsalingo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    Scaffold(
        containerColor = DuoDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dsalingo",
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        color = DuoGreen
                    )
                },
                actions = {
                    TextButton(onClick = onNavigateToLogin) {
                        Text("LOG IN", fontWeight = FontWeight.Black, color = DuoSubtext, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DuoDarkBg)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DuoDarkBg)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                // Mascot with dynamic emotion animation and speech bubble
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DuoCharacter(
                        emotion = CharacterEmotion.WELCOME,
                        size = 120.dp,
                        showAura = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CharacterSpeechBubble(
                        message = "Welcome to Dsalingoo! Ready to master Data Structures?",
                        title = "WELCOME!",
                        backgroundColor = DuoCardBg,
                        pointerDirection = BubblePointerDirection.UP,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "The free, fun, and effective way to learn Data Structures & Algorithms!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 28.dp),
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(36.dp))

                DuoButton(
                    text = "GET STARTED",
                    onClick = onNavigateToSignup,
                    color = DuoGreen,
                    shadowColor = DuoGreenDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                DuoSecondaryButton(
                    text = "I ALREADY HAVE AN ACCOUNT",
                    onClick = onNavigateToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                DuolingoFeaturesSection()
            }
        }
    }
}

@Composable
fun DuolingoFeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DuoCardBg)
            .padding(24.dp)
    ) {
        DuolingoFeatureItem("⚡", "Effective & Bite-Sized", "Master DSA with 5-minute interactive lessons every day.")
        Spacer(modifier = Modifier.height(20.dp))
        DuolingoFeatureItem("🎮", "Gamified Learning", "Earn XP, maintain streaks, and climb weekly leagues.")
        Spacer(modifier = Modifier.height(20.dp))
        DuolingoFeatureItem("📊", "Striver's A2Z Sheet", "Curated coding problems with visual array simulations.")
    }
}

@Composable
fun DuolingoFeatureItem(icon: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = DuoInputBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(icon, fontSize = 24.sp)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
            Text(desc, color = DuoSubtext, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}
