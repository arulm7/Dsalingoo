package com.app.dsalingo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.R
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D Pushable Duolingo Button Style
 * Matching DuolingoButtonStyle from Theme.swift
 */
@Composable
fun DuoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = DuoGreen,
    shadowColor: Color = DuoGreenDark,
    textColor: Color = Color.White,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    cornerRadius: Dp = 16.dp,
    fontSize: Int = 16,
    icon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentTopOffset = if (isPressed) 4.dp else 0.dp
    val effectiveShadowColor = if (enabled) shadowColor else Color(0xFF1C2A32)
    val effectiveFaceColor = if (enabled) color else Color(0xFF18252D)
    val effectiveTextColor = if (enabled) textColor else DuoSubtext

    Box(
        modifier = modifier
            .height(height + 4.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // 3D Depth Shadow Extrusion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = 4.dp)
                .background(effectiveShadowColor, RoundedCornerShape(cornerRadius))
        )

        // 3D Top Surface Button Face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = currentTopOffset)
                .background(effectiveFaceColor, RoundedCornerShape(cornerRadius))
                .border(
                    width = if (enabled) 0.dp else 1.dp,
                    color = DuoInputBorder,
                    shape = RoundedCornerShape(cornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    color = effectiveTextColor,
                    fontWeight = FontWeight.Black,
                    fontSize = fontSize.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DuoSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    DuoButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        color = DuoBlue,
        shadowColor = DuoBlueDark,
        enabled = enabled
    )
}

@Composable
fun DuoOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = DuoBlue,
    height: Dp = 48.dp
) {
    DuoButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        color = DuoCardBg,
        shadowColor = DuoInputBorder,
        textColor = textColor,
        height = height
    )
}

@Composable
fun DuoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                fontWeight = FontWeight.Bold,
                color = DuoPlaceholder,
                fontSize = 15.sp
            )
        },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DuoBlue,
            unfocusedBorderColor = DuoInputBorder,
            focusedContainerColor = DuoInputBg,
            unfocusedContainerColor = DuoInputBg,
            disabledContainerColor = DuoInputBg.copy(alpha = 0.5f),
            cursorColor = DuoBlue,
            focusedTextColor = DuoInputText,
            unfocusedTextColor = DuoInputText
        ),
        singleLine = singleLine
    )
}

// Custom Triangle Shape for Speech Bubbles pointing down
val SpeechBubblePointerDownShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width / 2f, size.height)
    lineTo(size.width, 0f)
    close()
}

/**
 * Speech Bubble Tooltip Component with Pointer Tip
 */
@Composable
fun DuoSpeechBubble(
    text: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    backgroundColor: Color = DuoGreen,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (content != null) {
                    content()
                } else {
                    Text(
                        text = text,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                }

                if (buttonText != null && onButtonClick != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onButtonClick() },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = buttonText.uppercase(),
                            color = backgroundColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        }

        // Pointer triangle pointing down
        Box(
            modifier = Modifier
                .size(width = 16.dp, height = 8.dp)
                .background(backgroundColor, SpeechBubblePointerDownShape)
        )
    }
}

/**
 * 3D Winding Path Node Button
 */
@Composable
fun Duo3DNodeButton(
    icon: String,
    isUnlocked: Boolean,
    isActive: Boolean,
    isCompleted: Boolean,
    themeColor: Color,
    themeDarkColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val faceColor = when {
        isCompleted -> DuoYellow
        isUnlocked -> themeColor
        else -> DuoCardBg
    }

    val shadowColor = when {
        isCompleted -> DuoYellowDark
        isUnlocked -> themeDarkColor
        else -> Color(0xFF142228)
    }

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier
            .size(86.dp)
            .clickable {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        // Active Outer Glowing Ring
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .scale(pulseScale)
                    .border(5.dp, themeColor.copy(alpha = 0.5f), CircleShape)
            )
        }

        // 3D Shadow Layer
        Box(
            modifier = Modifier
                .size(68.dp)
                .offset(y = if (isPressed) 2.dp else 7.dp)
                .background(shadowColor, CircleShape)
        )

        // 3D Top Surface
        Box(
            modifier = Modifier
                .size(68.dp)
                .offset(y = if (isPressed) 5.dp else 0.dp)
                .background(faceColor, CircleShape)
                .border(
                    width = 2.dp,
                    color = if (isUnlocked) Color.White.copy(alpha = 0.25f) else DuoInputBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = DuoSubtext.copy(alpha = 0.6f),
                    modifier = Modifier.size(26.dp)
                )
            } else if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text(
                    text = icon,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(120)
            isPressed = false
        }
    }
}

/**
 * Animated Duo Backpack Mascot (Matching DuoBackpackMascotView from SwiftUI)
 * Displays breathing idle bobbing, star sparkles, speech bubble, and tap spin!
 */
@Composable
fun DuoBackpackMascot(
    modifier: Modifier = Modifier,
    speechText: String = "LET'S GO!"
) {
    var showSpeechBubble by remember { mutableStateOf(false) }
    var tapSpinAngle by remember { mutableFloatStateOf(0f) }
    var starStep by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "mascotIdle")
    val mascotYOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )
    val auraPulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1100)
            starStep++
        }
    }

    val pathStars = listOf("⭐", "✨", "🌟", "💫")
    val currentStar = pathStars[starStep % pathStars.size]

    Box(
        modifier = modifier
            .size(100.dp)
            .clickable {
                showSpeechBubble = true
                tapSpinAngle = 360f
            },
        contentAlignment = Alignment.Center
    ) {
        // Glowing Aura Ellipse Shadow at base
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-6).dp)
                .size(width = 78.dp, height = 16.dp)
                .scale(auraPulseScale)
                .background(DuoGreen.copy(alpha = 0.35f), CircleShape)
        )

        // Sparkle Star Trail
        Text(
            text = currentStar,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = if (starStep % 2 == 0) (-14).dp else 4.dp)
        )

        // Speech Bubble on Tap / Hover
        AnimatedVisibility(
            visible = showSpeechBubble,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-46).dp)
        ) {
            DuoSpeechBubble(
                text = speechText,
                backgroundColor = DuoGreen
            )
        }

        // Duo Mascot Image / Vector
        Box(
            modifier = Modifier
                .size(86.dp)
                .offset(y = mascotYOffset.dp)
                .graphicsLayer {
                    rotationZ = tapSpinAngle
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.duo_backpack),
                contentDescription = "Duo Mascot",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
    }

    LaunchedEffect(showSpeechBubble) {
        if (showSpeechBubble) {
            delay(1800)
            showSpeechBubble = false
            tapSpinAngle = 0f
        }
    }
}

/**
 * Animated Lily Purple Mascot (Matching LilyPurpleMascotView from SwiftUI)
 */
@Composable
fun LilyPurpleMascot(modifier: Modifier = Modifier) {
    var showSpeechBubble by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "lilyIdle")
    val swayAngle by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    Box(
        modifier = modifier
            .size(90.dp)
            .clickable { showSpeechBubble = true },
        contentAlignment = Alignment.Center
    ) {
        // Aura
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-4).dp)
                .size(width = 74.dp, height = 14.dp)
                .background(DuoPurple.copy(alpha = 0.4f), CircleShape)
        )

        // Musical Notes
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("🎵", fontSize = 14.sp)
            Text("✨", fontSize = 12.sp)
            Text("🎶", fontSize = 14.sp)
        }

        AnimatedVisibility(
            visible = showSpeechBubble,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-46).dp)
        ) {
            DuoSpeechBubble(
                text = "MORE PRACTICE?",
                backgroundColor = DuoPurple
            )
        }

        // Mascot Shape Graphic
        Box(
            modifier = Modifier
                .size(76.dp)
                .graphicsLayer { rotationZ = swayAngle }
                .background(DuoPurple.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👧🏻", fontSize = 42.sp)
        }
    }

    LaunchedEffect(showSpeechBubble) {
        if (showSpeechBubble) {
            delay(1500)
            showSpeechBubble = false
        }
    }
}

/**
 * Animated Vikram & Bees Mascot (Matching VikramBeesMascotView from SwiftUI)
 */
@Composable
fun VikramBeesMascot(modifier: Modifier = Modifier) {
    var beeAngle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            beeAngle += 0.08f
        }
    }

    Box(
        modifier = modifier.size(90.dp),
        contentAlignment = Alignment.Center
    ) {
        // Orbiting Bees
        repeat(3) { index ->
            val angleOffset = index * (2f * Math.PI.toFloat() / 3f)
            val currentAngle = beeAngle + angleOffset
            val xPos = cos(currentAngle.toDouble()).toFloat() * 38f
            val yPos = sin(currentAngle.toDouble()).toFloat() * 18f - 20f

            Text(
                text = "🐝",
                fontSize = 14.sp,
                modifier = Modifier.offset(x = xPos.dp, y = yPos.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(76.dp)
                .background(DuoTeal.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👳🏽‍♂️", fontSize = 42.sp)
        }
    }
}

/**
 * Animated Oscar Artist Mascot (Matching OscarArtistMascotView from SwiftUI)
 */
@Composable
fun OscarArtistMascot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(90.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(DuoOrange.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🥸", fontSize = 42.sp)
        }
    }
}

/**
 * Animated Junior Party Mascot (Matching JuniorPartyMascotView from SwiftUI)
 */
@Composable
fun JuniorPartyMascot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(90.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(DuoBlue.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👦🏼", fontSize = 42.sp)
        }
    }
}
