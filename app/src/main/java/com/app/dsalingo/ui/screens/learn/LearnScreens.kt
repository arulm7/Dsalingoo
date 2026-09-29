package com.app.dsalingo.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.dsalingo.data.model.DataStructureCategory
import com.app.dsalingo.ui.components.*
import com.app.dsalingo.ui.theme.*

// Helper to get dynamic lesson count for UI path
fun getLessonCountForCategory(categoryId: String): Int {
    return when(categoryId) {
        "basics" -> 5
        "array" -> 10
        "string" -> 8
        "linkedlist" -> 10
        "stack" -> 6
        else -> 5
    }
}

fun getArrayLessonTitle(index: Int): String {
    return when(index) {
        0 -> "What is an Array?"
        1 -> "Array Indexing"
        2 -> "Accessing Elements"
        3 -> "Updating Elements"
        4 -> "Appending Elements"
        5 -> "Inserting Elements"
        6 -> "Deleting Elements"
        7 -> "Searching"
        8 -> "Time Complexity"
        9 -> "Array Boss Challenge"
        else -> "Lesson ${index + 1}"
    }
}

fun getArrayLessonIcon(index: Int): String {
    return when(index) {
        0 -> "⭐"
        1 -> "🏁"
        2 -> "🎯"
        3 -> "✏️"
        4 -> "➕"
        5 -> "🏃"
        6 -> "✂️"
        7 -> "🔍"
        8 -> "⏱️"
        9 -> "👑"
        else -> "🏆"
    }
}

@Composable
fun LearnScreen(
    onNavigateToCategory: (String) -> Unit,
    viewModel: LearnViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            ShimmerLearningPath()
        }
        is UiState.Error -> {
            ErrorStateView(
                message = state.message,
                onRetry = { viewModel.loadCategories() }
            )
        }
        is UiState.Success -> {
            val categories = state.data
            if (categories.isEmpty()) {
                EmptyStateView(
                    title = "No Categories Available",
                    message = "We couldn't find any learning categories. Please check back later.",
                    actionText = "RELOAD",
                    onAction = { viewModel.loadCategories() }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DuoDarkBg),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        DuolingoUnitBanner(
                            sectionNumber = 1,
                            unitNumber = 1,
                            title = "Data Structures & Algorithms",
                            subtitle = "Master the core building blocks of computer science",
                            themeColor = DuoGreen
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    itemsIndexed(categories) { index, category ->
                        val xOffset = when (index % 5) {
                            0 -> (-10).dp
                            1 -> (-45).dp
                            2 -> (-65).dp
                            3 -> 0.dp
                            4 -> 50.dp
                            else -> 0.dp
                        }

                        DuolingoCategoryNode(
                            category = category,
                            xOffset = xOffset,
                            isFirst = index == 0,
                            onClick = { onNavigateToCategory(category.id) }
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Unit Complete Divider
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(DuoInputBorder))
                            Text(
                                text = "SECTION 1 COMPLETE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = DuoSubtext,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(DuoInputBorder))
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
        else -> Unit
    }
}

@Composable
fun DuolingoCategoryNode(
    category: DataStructureCategory,
    xOffset: androidx.compose.ui.unit.Dp,
    isFirst: Boolean,
    onClick: () -> Unit
) {
    val color = Color(category.color)
    val darkColor = when(category.color.toLong()) {
        0xFF58CC02L -> DuoGreenDark
        0xFF1CB0F6L -> DuoBlueDark
        0xFFFF9600L -> DuoOrangeDark
        0xFFEA2B2BL -> DuoRedDark
        else -> color.copy(alpha = 0.8f)
    }

    val isCompleted = category.totalQuestions > 0 && category.completedQuestions >= category.totalQuestions
    val isActive = isFirst || (category.completedQuestions > 0 && !isCompleted)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = xOffset),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            // Active Start Tooltip Bubble
            if (isActive) {
                DuoSpeechBubble(
                    text = category.title,
                    buttonText = "START +10 XP",
                    onButtonClick = onClick,
                    backgroundColor = color,
                    modifier = Modifier
                        .width(190.dp)
                        .padding(bottom = 6.dp)
                )
            }

            // 3D Pushable Node
            Duo3DNodeButton(
                icon = category.icon,
                isUnlocked = true,
                isActive = isActive,
                isCompleted = isCompleted,
                themeColor = color,
                themeDarkColor = darkColor,
                onClick = onClick
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.title,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        // Display Mascot right next to the active node!
        if (isActive) {
            Spacer(modifier = Modifier.width(8.dp))
            DuoCharacter(
                emotion = CharacterEmotion.LESSON_START,
                size = 80.dp,
                showAura = true,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: String,
    onNavigateBack: () -> Unit,
    onNavigateToLesson: (String, String) -> Unit,
    viewModel: LearnViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            ShimmerLearningPath()
        }
        is UiState.Error -> {
            ErrorStateView(
                message = state.message,
                onRetry = { viewModel.loadCategories() }
            )
        }
        is UiState.Success -> {
            val category = state.data.find { it.id == categoryId }
            if (category == null) {
                ErrorStateView(
                    title = "CATEGORY NOT FOUND",
                    message = "We couldn't find the requested topic. Please return to the learn tab.",
                    canRetry = true,
                    onRetry = onNavigateBack
                )
            } else {
                val color = Color(category.color)
                val darkColor = when(category.color.toLong()) {
                    0xFF58CC02L -> DuoGreenDark
                    0xFF1CB0F6L -> DuoBlueDark
                    0xFFFF9600L -> DuoOrangeDark
                    0xFFEA2B2BL -> DuoRedDark
                    else -> color.copy(alpha = 0.8f)
                }

                val totalLessons = if (category.totalQuestions > 0) category.totalQuestions else 10
                val completedCount = category.completedQuestions
                var selectedLockedIndex by remember { mutableStateOf<Int?>(null) }

        Scaffold(
            containerColor = DuoDarkBg,
            topBar = {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(category.icon, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = category.title,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DuoDarkBg)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(DuoDarkBg),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    DuolingoUnitBanner(
                        sectionNumber = 1,
                        unitNumber = 1,
                        title = category.title,
                        subtitle = "Master essential operations & complexity of ${category.title}",
                        themeColor = color
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                }

                items(totalLessons) { i ->
                    val isLocked = i > completedCount
                    val isCompleted = i < completedCount
                    val isActive = i == completedCount
                    val isLockedSelected = selectedLockedIndex == i
                    val lessonTitle = if (categoryId == "array") getArrayLessonTitle(i) else "Lesson ${i + 1}"
                    val lessonIcon = if (categoryId == "array") getArrayLessonIcon(i) else when (i % 4) {
                        0 -> "⭐"
                        1 -> "📖"
                        2 -> "🎁"
                        else -> "🏆"
                    }

                    val xOffset = when (i % 5) {
                        0 -> (-10).dp
                        1 -> (-45).dp
                        2 -> (-65).dp
                        3 -> 0.dp
                        4 -> 50.dp
                        else -> 0.dp
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = xOffset),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            // Active Lesson Tooltip
                            if (isActive && selectedLockedIndex == null) {
                                DuoSpeechBubble(
                                    text = lessonTitle,
                                    buttonText = if (i == 9) "BOSS BATTLE 👑" else "START +10 XP",
                                    onButtonClick = { onNavigateToLesson(categoryId, "lesson_$i") },
                                    backgroundColor = color,
                                    modifier = Modifier.width(190.dp).padding(bottom = 6.dp)
                                )
                            }

                            // Locked Popover Tooltip
                            if (isLockedSelected) {
                                DuoSpeechBubble(
                                    text = "$lessonTitle is Locked",
                                    buttonText = "LOCKED",
                                    onButtonClick = {},
                                    backgroundColor = DuoCardBg,
                                    textColor = DuoSubtext,
                                    modifier = Modifier.width(200.dp).padding(bottom = 6.dp)
                                ) {
                                    Text(
                                        text = "Complete all levels above to unlock this!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DuoSubtext,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Duo3DNodeButton(
                                icon = lessonIcon,
                                isUnlocked = !isLocked,
                                isActive = isActive,
                                isCompleted = isCompleted,
                                themeColor = color,
                                themeDarkColor = darkColor,
                                onClick = {
                                    if (isLocked) {
                                        selectedLockedIndex = if (selectedLockedIndex == i) null else i
                                    } else {
                                        selectedLockedIndex = null
                                        onNavigateToLesson(categoryId, "lesson_$i")
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lessonTitle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isLocked) DuoSubtext else Color.White
                            )
                        }

                        // Mascot beside active lesson node
                        if (isActive) {
                            Spacer(modifier = Modifier.width(8.dp))
                            DuoCharacter(
                                emotion = if (i == 9) CharacterEmotion.BOSS else CharacterEmotion.LESSON_START,
                                size = 80.dp,
                                showAura = true,
                                onClick = { onNavigateToLesson(categoryId, "lesson_$i") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(DuoInputBorder))
                        Text(
                            text = "UNIT COMPLETE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoSubtext,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(DuoInputBorder))
                    }
                }
            }
        }
    }
}
        else -> Unit
    }
}

/**
 * Duolingo Unit Header Card matching SwiftUI unitSectionView
 */
@Composable
fun DuolingoUnitBanner(
    sectionNumber: Int,
    unitNumber: Int,
    title: String,
    subtitle: String,
    themeColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        color = themeColor,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SECTION $sectionNumber, UNIT $unitNumber".uppercase(),
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.clickable { /* Open guidebook */ }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📖",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "GUIDEBOOK",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}
